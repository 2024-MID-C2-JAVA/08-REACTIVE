package co.sofka.handler;


import co.sofka.*;
import co.sofka.appservice.eventsUseCase.CreateAccountCommandUseCaseImpl;
import co.sofka.appservice.eventsUseCase.CreateUserCommandUseCaseImpl;
import co.sofka.appservice.user.AuthenticateUseCaseImpl;
import co.sofka.appservice.user.GetUserByEmailUseCaseImpl;
import co.sofka.appservice.user.RegisterUseCaseImpl;
import co.sofka.commands.CreateUserCommand;
import co.sofka.data.UserIdDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

    private final AuthenticateUseCaseImpl authenticateUseCase;
    private final CreateUserCommandUseCaseImpl createAccountEventUseCase;

    public UserHandler(AuthenticateUseCaseImpl authenticateUseCase, CreateUserCommandUseCaseImpl createAccountEventUseCase) {
        this.authenticateUseCase = authenticateUseCase;
        this.createAccountEventUseCase = createAccountEventUseCase;
    }

    public Mono<ServerResponse> authenticate(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<AuthenticationRequest>>() {
                })
                .flatMap(user -> authenticateUseCase.apply(user.getDinBody())
                        .flatMap(userAuthenticated -> {
                            ResponseMs<AuthenticationResponse> din = new ResponseMs<>();
                            din.setDinHeader(user.getDinHeader());
                            din.setDinBody(userAuthenticated);
                            din.setDinError(new DinError(DinErrorEnum.SUCCESS));
                            return ServerResponse.status(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(din);
                        }));
    }

    public Mono<ServerResponse> register(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<UserRequest>>() {
                })
                .flatMap(user -> {
                    CreateUserCommand command = new CreateUserCommand();

                    command.setFirstname(user.getDinBody().getFirstname());
                    command.setLastname(user.getDinBody().getLastname());
                    command.setEmail(user.getDinBody().getEmail());
                    command.setPassword(user.getDinBody().getPassword());
                    command.setRole(user.getDinBody().getRole());

                    return createAccountEventUseCase.publish(Mono.just(command))
                            .flatMap(userRegistered -> ServerResponse
                                    .status(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(userRegistered));
                });
    }

}
