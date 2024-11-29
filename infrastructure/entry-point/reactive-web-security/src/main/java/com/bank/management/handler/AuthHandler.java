package com.bank.management.handler;

import com.bank.management.command.CreateUserCommand;
import com.bank.management.JwtUtil;
import com.bank.management.ResponseBuilder;
import com.bank.management.data.AuthRequestDTO;
import com.bank.management.data.RegisterRequestDTO;
import com.bank.management.data.RequestMs;
import com.bank.management.enums.DinErrorCode;
import com.bank.management.exception.BadCredentialException;
import com.bank.management.exception.UserAlreadyExistsException;
import com.bank.management.gateway.CustomerRepository;
import com.bank.management.usecase.appservice.CreateUserEventUseCase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AuthHandler {

    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final CreateUserEventUseCase createUserUseCaseEvent;
    private final CustomerRepository customerRepository;

    public AuthHandler(ReactiveAuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, CreateUserEventUseCase createUserUseCaseEvent, CustomerRepository customerRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.createUserUseCaseEvent = createUserUseCaseEvent;
        this.customerRepository = customerRepository;
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<AuthRequestDTO>>() {})
                .flatMap(authRequest -> authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequest.getDinBody().getUsername(),
                                authRequest.getDinBody().getPassword()
                        )
                ).flatMap(authentication -> {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    String token = jwtUtil.generateToken(userDetails);

                    return customerRepository.findByUsername(userDetails.getUsername())
                            .switchIfEmpty(Mono.error(new BadCredentialException()))
                            .flatMap(customer -> {
                                Map<String, String> responseData = new HashMap<>();
                                responseData.put("token", token);
                                responseData.put("customerId", customer.getId().value());

                                return ServerResponse.ok().bodyValue(
                                        Objects.requireNonNull(ResponseBuilder.buildResponse(
                                                authRequest.getDinHeader(),
                                                responseData,
                                                DinErrorCode.SUCCESS,
                                                HttpStatus.OK,
                                                "Authentication successful."
                                        ).getBody())
                                );
                            });
                }))
                .onErrorResume(AuthenticationException.class, e -> HandleError.handle(request, DinErrorCode.BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED, "Authentication failed. Invalid username or password.")
                )
                .onErrorResume(BadCredentialException.class, e -> HandleError.handle(request, DinErrorCode.BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED, "Authentication failed. Invalid username or password.")
                );
    }

    public Mono<ServerResponse> register(ServerRequest request) {
        return request.bodyToMono((new ParameterizedTypeReference<RequestMs<RegisterRequestDTO>>() {}))
                .flatMap(registerRequest -> {
                    RegisterRequestDTO dto = registerRequest.getDinBody();

                    CreateUserCommand command = new CreateUserCommand(
                            dto.getName(),
                            dto.getLastname(),
                            dto.getUsername(),
                            dto.getPassword(),
                            dto.getRoles()
                    );

                    return createUserUseCaseEvent.apply(command)
                            .flatMap(userCreated -> authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                    command.getUsername(),
                                                    registerRequest.getDinBody().getPassword(),
                                                    registerRequest.getDinBody().getRoles().stream()
                                                            .map(SimpleGrantedAuthority::new)
                                                            .collect(Collectors.toList())
                                            )
                                    )
                                    .flatMap(authentication -> {
                                        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                                        String token = jwtUtil.generateToken(userDetails);

                                        return customerRepository.findByUsername(command.getUsername())
                                                .flatMap(customerFound -> {
                                                    Map<String, String> responseData = new HashMap<>();
                                                    responseData.put("token", token);
                                                    responseData.put("customerId", customerFound.getId().value());

                                                    return ServerResponse.status(HttpStatus.CREATED).bodyValue(
                                                            Objects.requireNonNull(ResponseBuilder.buildResponse(
                                                                    registerRequest.getDinHeader(),
                                                                    responseData,
                                                                    DinErrorCode.SUCCESS,
                                                                    HttpStatus.CREATED,
                                                                    "User created and authenticated successfully."
                                                            ).getBody())
                                                    );
                                                });
                                    }));
                })
                .onErrorResume(UserAlreadyExistsException.class, e -> HandleError.handle(request, DinErrorCode.OPERATION_FAILED, HttpStatus.CONFLICT, e.getMessage()))
                .onErrorResume(IllegalArgumentException.class, e -> HandleError.handle(request, DinErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST, "Invalid input data."))
                .onErrorResume(e -> HandleError.handle(request, DinErrorCode.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));

    }
}
