package co.sofka.command.create;

import co.sofka.command.dto.request.RequestMs;
import co.sofka.command.dto.request.TokenInicilizer;
import co.sofka.command.dto.response.DinError;
import co.sofka.command.dto.response.ResponseMs;
import co.sofka.command.dto.response.TokenResponse;
import co.sofka.config.TokenByDinHeaders;
import co.sofka.security.configuration.jwt.JwtService;
import co.sofka.security.configuration.jwt.TokenProvider;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class TokenGenerateHandler {

    private static final Logger logger = LoggerFactory.getLogger(TokenGenerateHandler.class);

    //IGetCustomerByUserNameService getCustomerByUserNameService;

   private final TokenByDinHeaders utils;

    private final JwtService jwtUtils;


    private final PasswordEncoder passwordEncoder;
//    private final ReactiveUserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;

    private final MapReactiveUserDetailsService userDetailsService;

    public Mono<ResponseMs<TokenResponse>> apply(RequestMs<TokenInicilizer> request) {

        ResponseMs<TokenResponse> responseMs = new ResponseMs<>();
        responseMs.setDinHeader(request.getDinHeader());
        DinError error = new DinError();
        responseMs.setDinError(error);

//        Customer byUsername = getCustomerByUserNameService.findByUsername(request.getDinBody().getUsername()).block();

//        Customer byUsername = null;
//
//        if (byUsername == null) {
//            throw new CustomerNotExistException("Customer no definido para estos parametros.",request.getDinHeader(),1004);
//        }
//
//
//        Boolean b = jwtUtils.matchesPasswd(request.getDinBody().getPassword(), byUsername.getPwd());
//
//        logger.info("Password Matches : {}",b);
//
//        if (!b) {
//            throw new PasswordIncorrectoException("Datos Incorrecto",request.getDinHeader(),1005);
//        }
//
//        LoginPartnerRequest loginPartnerRequest = new LoginPartnerRequest();
//        loginPartnerRequest.setUsername(request.getDinBody().getUsername());
//        loginPartnerRequest.setRol(byUsername.getRol());
//        logger.info("Rol : {}",byUsername.getRol());
//        List<String> write = List.of("WRITE", "READ", byUsername.getRol());
//        loginPartnerRequest.setPermisos(write);

//        UserDetails block = userDetailsService.findByUsername(request.getDinBody().getUsername()).block();

        var user = User.builder()
                .username("pablo")
                .password(jwtUtils.encryptionPassword("12qwaszx"))
                .roles("USER")
                .build();
//
//        Mono<ResponseMs<TokenResponse>> map = userDetailsService.findByUsername("adamk")
//                .filter(u -> passwordEncoder.matches("password", u.getPassword()))
//                .map(u -> tokenProvider.generateToken(u))
//                .map(token -> {
//                    TokenResponse tokenResponse = new TokenResponse();
//                    tokenResponse.setToken(token);
//                    responseMs.setDinBody(tokenResponse);
//                    return responseMs;
//                });

//        tokenProvider.generateToken(user);

        String tokenString = jwtUtils.generateToken(user);

        logger.info("Token : {}",tokenString);

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(tokenString);

        responseMs.setDinBody(tokenResponse);

        return Mono.just(responseMs);
    }



}
