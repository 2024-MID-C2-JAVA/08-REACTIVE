package co.sofka.appservice.user;

import co.sofka.AuthenticationRequest;
import co.sofka.AuthenticationResponse;
import co.sofka.jwt.UserRepository;
import reactor.core.publisher.Mono;

public class AuthenticateUseCaseImpl {
    private final UserRepository userRepository;

    public AuthenticateUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<AuthenticationResponse> apply(AuthenticationRequest authenticationRequest) {
        return userRepository.authenticate(authenticationRequest);
    }
}
