package co.sofka;


import co.sofka.command.create.TokenGenerateHandler;
import co.sofka.command.dto.request.RequestMs;
import co.sofka.command.dto.request.TokenInicilizer;
import co.sofka.command.dto.response.LoginResponse;
import co.sofka.command.dto.response.ResponseMs;
import co.sofka.command.dto.response.TokenResponse;
import co.sofka.security.configuration.jwt.JwtService;
import co.sofka.security.configuration.jwt.TokenProvider;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/utils")
@AllArgsConstructor
public class UtilsController {

    private static final Logger logger = LoggerFactory.getLogger(UtilsController.class);

    private final TokenGenerateHandler handler;

    private final PasswordEncoder passwordEncoder;
    private final ReactiveUserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;

    private final JwtService jwtUtils;

    @PostMapping("/login")
    Mono<LoginResponse> login(@RequestBody RequestMs<TokenInicilizer> request) {
        return userDetailsService.findByUsername(request.getDinBody().getUsername())
                .map(u -> {
                    logger.info("Users {} {}",u.getUsername(),u.getPassword());
                    logger.info("Password {} {}",passwordEncoder.encode(request.getDinBody().getPassword()), passwordEncoder.matches(request.getDinBody().getPassword(), u.getPassword()));
                    return u;
                })
                .filter(u -> passwordEncoder.matches(request.getDinBody().getPassword(), u.getPassword()))
                .map(u -> {
                    logger.info("Usuario autenticado {}",u);
                    String s = jwtUtils.generateTokenJWT(Map.of("sub","READ"),u);
                    logger.info("Token generado {}",s);
                    return s;

                })
                .map(token->{
                    logger.info("Token retornada {}",token);
                    LoginResponse loginResponse = new LoginResponse();
                    loginResponse.setToken(token);
                    return loginResponse;
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)));
    }

    @PostMapping("/generate")
    public Mono<ResponseMs<TokenResponse>> generateToken(@RequestBody RequestMs<TokenInicilizer> request) {
        logger.info("Buscando todos los Customer");
        return handler.apply(request);
    }



}
