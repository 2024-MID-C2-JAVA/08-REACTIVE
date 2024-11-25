package co.sofka;


import co.sofka.data.UserIdDto;
import co.sofka.handler.UserHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserHandler userHandler;

    public UserController(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<ResponseMs<AuthenticationResponse>>> register(@RequestBody RequestMs<UserRequest> request) {
        return userHandler.register(request.getDinBody())
                .map(authResponse -> ResponseEntity.ok(new ResponseMs<>(request.getDinHeader(), authResponse, new DinError(DinErrorEnum.SUCCESS))))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseMs<>(request.getDinHeader(), null, new DinError(DinErrorEnum.OPERATION_FAILED)))));
    }

    @PostMapping("/authenticate")
    public Mono<ResponseEntity<ResponseMs<AuthenticationResponse>>> authenticate(
            @RequestBody RequestMs<AuthenticationRequest> request) {

        return  userHandler.authenticate(request.getDinBody())
                .map(authResponse -> ResponseEntity.ok(new ResponseMs<>(request.getDinHeader(), authResponse, new DinError(DinErrorEnum.SUCCESS))))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseMs<>(request.getDinHeader(), null, new DinError(DinErrorEnum.OPERATION_FAILED)))));
    }

    @PostMapping("/getUserById")
    public Mono<ResponseEntity<ResponseMs<UserRequest>>> getUser(@RequestBody RequestMs<UserIdDto> dto) {
        return userHandler.getUserByEmail(dto.getDinBody())  // Mono<UserRequest>
                .map(userRequest -> ResponseEntity.ok(new ResponseMs<>(dto.getDinHeader(), userRequest, new DinError(DinErrorEnum.SUCCESS))))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseMs<>(dto.getDinHeader(), null, new DinError(DinErrorEnum.OPERATION_FAILED)))));
    }

}
