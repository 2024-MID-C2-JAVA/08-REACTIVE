package com.bank.management.interceptor;

import com.bank.management.context.DinHeaderContext;
import com.bank.management.data.DinHeader;
import com.bank.management.data.RequestMs;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class DinHeaderFilter implements WebFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    byte[] bodyBytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bodyBytes);
                    DataBufferUtils.release(dataBuffer); // Libera el buffer para evitar fugas

                    String body = new String(bodyBytes,
                            exchange.getRequest().getHeaders().getContentType() != null &&
                                    exchange.getRequest().getHeaders().getContentType().getCharset() != null
                                    ? exchange.getRequest().getHeaders().getContentType().getCharset()
                                    : StandardCharsets.UTF_8);

                    try {
                        if (!body.isEmpty()) {
                            RequestMs<?> requestMs = objectMapper.readValue(body, RequestMs.class);
                            DinHeader dinHeader = requestMs.getDinHeader();
                            if (dinHeader != null) {
                                DinHeaderContext.setDinHeader(dinHeader);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Reinyectar el cuerpo de la solicitud
                    ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return Flux.just(exchange.getResponse().bufferFactory().wrap(bodyBytes));
                        }
                    };

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                })
                .doFinally(signalType -> DinHeaderContext.clear());
    }
}
