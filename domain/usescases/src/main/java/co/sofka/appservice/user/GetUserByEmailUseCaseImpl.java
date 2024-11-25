package co.sofka.appservice.user;

import co.sofka.AuthenticationRequest;
import co.sofka.UserRequest;
import co.sofka.jwt.UserRepository;
import co.sofka.jwt.UserViewsRepository;
import reactor.core.publisher.Mono;

public class GetUserByEmailUseCaseImpl {

    private final UserViewsRepository userRepository;

    public GetUserByEmailUseCaseImpl(UserViewsRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<UserRequest> apply(AuthenticationRequest authenticationRequest) {
        return userRepository.getUserByEmail(authenticationRequest);
    }
}
