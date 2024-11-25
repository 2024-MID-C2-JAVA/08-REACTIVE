package co.sofka.jwt;

import co.sofka.AuthenticationRequest;
import co.sofka.AuthenticationResponse;
import co.sofka.UserRequest;
import reactor.core.publisher.Mono;

public interface UserViewsRepository {
    Mono<UserRequest>getUserByEmail(AuthenticationRequest authenticationRequest);
}
