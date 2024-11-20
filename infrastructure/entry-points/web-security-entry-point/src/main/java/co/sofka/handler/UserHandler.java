package co.sofka.handler;


import co.sofka.AuthenticationRequest;
import co.sofka.AuthenticationResponse;
import co.sofka.UserRequest;
import co.sofka.appservice.user.AuthenticateUseCaseImpl;
import co.sofka.appservice.user.GetUserByEmailUseCaseImpl;
import co.sofka.appservice.user.RegisterUseCaseImpl;
import co.sofka.data.UserIdDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

    private final RegisterUseCaseImpl registerUseCase;
    private final GetUserByEmailUseCaseImpl getUserByEmailUseCase;
    private final AuthenticateUseCaseImpl authenticateUseCase;

    public UserHandler(RegisterUseCaseImpl registerUseCase, GetUserByEmailUseCaseImpl getUserByEmailUseCase, AuthenticateUseCaseImpl authenticateUseCase) {
        this.registerUseCase = registerUseCase;
        this.getUserByEmailUseCase = getUserByEmailUseCase;
        this.authenticateUseCase = authenticateUseCase;
    }

    public Mono<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        return authenticateUseCase.apply(authenticationRequest);
    }

    public Mono<AuthenticationResponse> register(UserRequest userRequest) {
        return registerUseCase.apply(userRequest);
    }

    public Mono<UserRequest> getUserByEmail(UserIdDto userIdDto) {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(userIdDto.getId());
        return getUserByEmailUseCase.apply(authenticationRequest);
    }
}
