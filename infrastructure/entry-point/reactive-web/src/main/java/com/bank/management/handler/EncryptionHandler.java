package com.bank.management.handler;

import com.bank.management.ResponseBuilder;
import com.bank.management.data.RequestMs;
import com.bank.management.enums.DinErrorCode;
import com.bank.management.usecase.appservice.EncryptionUseCase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class EncryptionHandler {

    private final EncryptionUseCase encryptionUseCase;

    public EncryptionHandler(EncryptionUseCase encryptionUseCase) {
        this.encryptionUseCase = encryptionUseCase;
    }

    public Mono<ServerResponse> encryptData(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<Map<String, String>>>() {})
                .flatMap(req -> {
                    Map<String, String> encryptedData = new HashMap<>();
                    try {
                        String key = req.getDinHeader().getSymmetricKey();
                        String iv = req.getDinHeader().getInitializationVector();

                        for (Map.Entry<String, String> entry : req.getDinBody().entrySet()) {
                            String dataKey = entry.getKey();
                            String value = entry.getValue();
                            String encryptedValue = encryptionUseCase.encryptData(value, key, iv);
                            encryptedData.put(dataKey, encryptedValue);
                        }

                        return ServerResponse.ok()
                                .bodyValue(ResponseBuilder.buildResponse(
                                        req.getDinHeader(),
                                        encryptedData,
                                        DinErrorCode.SUCCESS,
                                        HttpStatus.OK,
                                        "Data encrypted successfully."
                                ));
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                })
                .onErrorResume(e -> HandleError.handle(request, DinErrorCode.ERROR_ENCRYPTING_DATA, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}
