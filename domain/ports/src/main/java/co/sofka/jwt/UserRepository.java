package co.sofka.jwt;

import co.sofka.AuthenticationRequest;
import co.sofka.AuthenticationResponse;
import co.sofka.UserRequest;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<AuthenticationResponse>register(UserRequest userRequest);
    Mono<AuthenticationResponse>authenticate(AuthenticationRequest authenticationRequest);
    Mono<UserRequest>getUserByEmail(AuthenticationRequest authenticationRequest);
}
