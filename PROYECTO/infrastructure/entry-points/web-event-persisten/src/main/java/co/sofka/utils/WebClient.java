package co.sofka.utils;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.util.List;

@Component
public class WebClient {
    private static final Logger logger = LoggerFactory.getLogger(WebClient.class);


    public String post(String urlString, String arguments, List<String> headers) throws Exception {

        logger.info("API-REQUEST: POST {}", urlString);

        try {
            // Construcción de cabeceras personalizadas
            var customHeaders = constructHeaders(headers);

            // Construcción del WebClient con el HttpClient configurado
            var webClient = createWebClient();

            // Realizar la solicitud POST con timeout explícito
            var weClientResponse = webClient.post()
                    .uri(urlString)
                    .headers(httpHeaders -> httpHeaders.addAll(customHeaders))
                    .bodyValue(arguments)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(30)) // Timeout para la operación completa
                    .onErrorResume(WebClientResponseException.class, e -> {
                        if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                            String responseBody = e.getResponseBodyAsString();
                            return Mono.just(responseBody);
                        }
                        var message = String.format("StatusCode %s StatusText %s", e.getStatusCode(), e.getStatusText());
                        logger.error(message);
                        throw new RuntimeException(message);
                    })

//                    .block()
                    ;

            logger.info("API-RESPONSE: POST {} response {}", urlString, weClientResponse);
            return weClientResponse.toString();

        } catch (Exception e) {
            String message = String.format("API-EXCEPTION: POST %s error %s", urlString, e.getMessage());
            logger.error(message);
            throw new Exception(message);
        }
    }

    private HttpHeaders constructHeaders(List<String> headers) {
        var customHeaders = new HttpHeaders();
        customHeaders.add("User-Agent", "Java/" + System.getProperty("java.version"));
        customHeaders.add("Content-Type", "application/json");
        customHeaders.add("Accept", "application/json");
        if (headers != null) {
            for (String header : headers) {
                String[] headerSplit = header.split(":");
                customHeaders.add(headerSplit[0], headerSplit[1]);
            }
        }
        return customHeaders;
    }


    private org.springframework.web.reactive.function.client.WebClient createWebClient() throws SSLException {
        // Configuración del ConnectionProvider para la gestión de conexiones HTTP
        ConnectionProvider provider = createProvider();

        // Configuración del contexto SSL con soporte para StartTLS
        SslContext sslContext = createSslContext();

        // Configuración del HttpClient con timeouts explícitos
        HttpClient httpClient = createHttpClient(provider, sslContext);

        return org.springframework.web.reactive.function.client.WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    private ConnectionProvider createProvider() {
        return ConnectionProvider.builder("fixed")
                .maxConnections(500) // Máximo de conexiones permitidas
                .maxIdleTime(Duration.ofSeconds(20)) // Tiempo máximo de inactividad de las conexiones
                .maxLifeTime(Duration.ofSeconds(60)) // Tiempo máximo de vida de una conexión
                .pendingAcquireTimeout(Duration.ofSeconds(60)) // Tiempo máximo para adquirir una conexión
                .evictInBackground(Duration.ofSeconds(120)) // Tiempo para limpiar conexiones inactivas en segundo plano
                .build();
    }

    private SslContext createSslContext() throws SSLException {
        return SslContextBuilder.forClient()
                .startTls(true) // Habilita StartTLS para conexiones seguras
                .build();
    }


    private HttpClient createHttpClient(ConnectionProvider provider, SslContext sslContext) {
        return HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000) // Timeout para establecer la conexión (30 segundos)
                .secure(ssl -> ssl.sslContext(sslContext)) // Configuración del contexto SSL
                .responseTimeout(Duration.ofSeconds(30)) // Timeout para recibir la respuesta
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(30)) // Timeout de lectura (30 segundos)
                        .addHandlerLast(new WriteTimeoutHandler(30)) // Timeout de escritura (30 segundos)
                );
    }
}
