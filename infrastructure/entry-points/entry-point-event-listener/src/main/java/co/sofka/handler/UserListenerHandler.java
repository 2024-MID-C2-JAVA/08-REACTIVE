package co.sofka.handler;


import co.sofka.AuthenticationRequest;
import co.sofka.RequestMs;
import co.sofka.UserRequest;
import co.sofka.appservice.user.AuthenticateUseCaseImpl;
import co.sofka.appservice.user.RegisterUseCaseImpl;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserListenerHandler {

    private final RegisterUseCaseImpl registerUseCase;
    private final AuthenticateUseCaseImpl authenticateUseCase;

    public UserListenerHandler(RegisterUseCaseImpl registerUseCase, AuthenticateUseCaseImpl authenticateUseCase) {
        this.registerUseCase = registerUseCase;
        this.authenticateUseCase = authenticateUseCase;
    }

    public Mono<ServerResponse> authenticate(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<AuthenticationRequest>>() {
                })
                .flatMap(user -> {
                    return authenticateUseCase.apply(user.getDinBody())
                            .flatMap(userAuthenticated -> ServerResponse.status(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(user));
                });
    }

    public Mono<ServerResponse> register(UserRequest user) {
        return registerUseCase.apply(user)
                .flatMap(userCreated->ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user));
    }

}
