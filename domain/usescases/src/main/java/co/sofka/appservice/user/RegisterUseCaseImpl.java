package co.sofka.appservice.user;

import co.sofka.AuthenticationResponse;
import co.sofka.UserRequest;
import co.sofka.jwt.UserRepository;
import co.sofka.rabbitMq.CreateUserEventUseCase;
import reactor.core.publisher.Mono;

public class RegisterUseCaseImpl {

    private final UserRepository userRepository;
    private final CreateUserEventUseCase event;

    public RegisterUseCaseImpl(UserRepository userRepository, CreateUserEventUseCase event) {
        this.userRepository = userRepository;
        this.event = event;
    }

    public Mono<AuthenticationResponse> apply(UserRequest userRequest) {
        return userRepository.register(userRequest);
    }
}
