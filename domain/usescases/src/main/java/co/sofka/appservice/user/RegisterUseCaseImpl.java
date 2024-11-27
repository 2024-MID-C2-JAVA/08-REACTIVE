package co.sofka.appservice.user;

import co.sofka.AuthenticationResponse;
import co.sofka.UserRequest;
import co.sofka.jwt.UserRepository;
import co.sofka.rabbitMq.CreateUserEventUseCase;
import reactor.core.publisher.Mono;

public class RegisterUseCaseImpl {

    private final UserRepository userRepository;

    public RegisterUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<AuthenticationResponse> apply(UserRequest userRequest) {
        return userRepository.register(userRequest);
    }
}
