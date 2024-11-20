package co.sofka.command.create;

import co.sofka.Customer;
import co.sofka.command.dto.request.RequestMs;
import co.sofka.command.dto.request.TokenInicilizer;
import co.sofka.command.dto.response.DinError;
import co.sofka.command.dto.response.ResponseMs;
import co.sofka.command.dto.response.TokenResponse;
import co.sofka.config.TokenByDinHeaders;
import co.sofka.middleware.CustomerNotExistException;
import co.sofka.middleware.PasswordIncorrectoException;
import co.sofka.security.configuration.entity.LoginPartnerRequest;
import co.sofka.security.configuration.jwt.JwtUtils;
import co.sofka.usecase.appBank.IGetCustomerByUserNameService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@AllArgsConstructor
public class TokenGenerateHandler {

    private static final Logger logger = LoggerFactory.getLogger(TokenGenerateHandler.class);

    IGetCustomerByUserNameService getCustomerByUserNameService;

   private final TokenByDinHeaders utils;

    private final JwtUtils jwtUtils;

    public Mono<ResponseMs<TokenResponse>> apply(RequestMs<TokenInicilizer> request) {

        ResponseMs<TokenResponse> responseMs = new ResponseMs<>();
        responseMs.setDinHeader(request.getDinHeader());
        DinError error = new DinError();
        responseMs.setDinError(error);

        Customer byUsername = getCustomerByUserNameService.findByUsername(request.getDinBody().getUsername()).block();

        if (byUsername == null) {
            throw new CustomerNotExistException("Customer no definido para estos parametros.",request.getDinHeader(),1004);
        }


        Boolean b = jwtUtils.matchesPasswd(request.getDinBody().getPassword(), byUsername.getPwd());

        logger.info("Password Matches : {}",b);

        if (!b) {
            throw new PasswordIncorrectoException("Datos Incorrecto",request.getDinHeader(),1005);
        }

        LoginPartnerRequest loginPartnerRequest = new LoginPartnerRequest();
        loginPartnerRequest.setUsername(request.getDinBody().getUsername());
        loginPartnerRequest.setRol(byUsername.getRol());
        logger.info("Rol : {}",byUsername.getRol());
        List<String> write = List.of("WRITE", "READ", byUsername.getRol());
        loginPartnerRequest.setPermisos(write);
        String tokenString = jwtUtils.generateJwtToken(loginPartnerRequest);

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(tokenString);

        responseMs.setDinBody(tokenResponse);

        return Mono.just(responseMs);
    }



}
