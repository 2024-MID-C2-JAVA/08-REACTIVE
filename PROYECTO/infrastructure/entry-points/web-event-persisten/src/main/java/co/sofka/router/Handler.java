package co.sofka.router;

import co.sofka.Account;
import co.sofka.Customer;
import co.sofka.Event;
import co.sofka.command.dto.request.RequestMs;
import co.sofka.dto.BankTransactionDepositCajero;
import co.sofka.dto.BankTransactionDepositSucursal;
import co.sofka.dto.BankTransactionDepositTransfer;
import co.sofka.usecase.appEventBank.ISaveEventTransactionDepositCajeroService;
import co.sofka.usecase.appEventBank.ISaveEventTransactionDepositSucursalService;
import co.sofka.usecase.appEventBank.ISaveEventTransactionDepositTransferenciaService;
import co.sofka.usecase.appEventBank.SaveEventCustomerUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Handler {

    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    private  final SaveEventCustomerUseCase saveCustomerUseCase;

    private final ISaveEventTransactionDepositSucursalService saveEventTransactionDepositSucursalService;

    private final ISaveEventTransactionDepositCajeroService saveEventTransactionDepositCajeroService;

    private final ISaveEventTransactionDepositTransferenciaService saveEventTransactionDepositTransferenciaService;


    public Mono<ServerResponse> SaveCustomerUseCase(ServerRequest serverRequest) {
        logger.info("Handler - SaveCustomerUseCase");
        Mono<RequestMs> responseMsMono = serverRequest.bodyToMono(RequestMs.class);


        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(saveCustomerUseCase.apply( responseMsMono.map(item->{
                    Map<String, Object> map = (Map<String, Object>) item.getDinBody();
                    logger.info("Handler - SaveCustomerUseCase - map {}", map);
                    Customer customer = new Customer();
                    customer.setUsername( map.get("username").toString());
                    customer.setPwd(map.get("pwd").toString());
                    customer.setRol(map.get("rol").toString());

                    List<Account>   lista = (List<Account>) map.get("accounts");

                    customer.setAccounts(lista);

                    return customer;
                })), Event.class));
    }



    public Mono<ServerResponse> SaveDepositarSucursalUseCase(ServerRequest serverRequest) {
        logger.info("Handler - SaveDepositarSucursalUseCase");
        Mono<RequestMs> responseMsMono = serverRequest.bodyToMono(RequestMs.class);


        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(saveEventTransactionDepositSucursalService.apply( responseMsMono.map(item->{
                    Map<String, Object> map = (Map<String, Object>) item.getDinBody();
                    logger.info("Handler - SaveCustomerUseCase - map {}", map);
                    BankTransactionDepositSucursal transaction = new BankTransactionDepositSucursal();
                    transaction.setCustomerId( map.get("customerId").toString());
                    transaction.setAccountNumberClient(map.get("accountNumberClient").toString());
                    transaction.setAmount(BigDecimal.valueOf(Double.parseDouble(map.get("amount").toString())));

                    return transaction;
                })), Event.class));
    }


    public Mono<ServerResponse> SaveDepositarCajeroUseCase(ServerRequest serverRequest) {
        logger.info("Handler - SaveDepositarCajeroUseCase");
        Mono<RequestMs> responseMsMono = serverRequest.bodyToMono(RequestMs.class);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(saveEventTransactionDepositCajeroService.apply( responseMsMono.map(item->{
                    Map<String, Object> map = (Map<String, Object>) item.getDinBody();
                    logger.info("Handler - SaveDepositarCajeroUseCase - map {}", map);
                    BankTransactionDepositCajero transaction = new BankTransactionDepositCajero();

                    transaction.setCustomerId( map.get("customerId").toString());
                    transaction.setAccountNumberClient(map.get("accountNumberClient").toString());
                    transaction.setAmount(BigDecimal.valueOf(Double.parseDouble(map.get("amount").toString())));


                    return transaction;
                })), Event.class));
    }


    public Mono<ServerResponse> SaveDepositarTransferenciaUseCase(ServerRequest serverRequest) {
        logger.info("Handler - SaveDepositarTRansferenciaUseCase");
        Mono<RequestMs> responseMsMono = serverRequest.bodyToMono(RequestMs.class);


        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(saveEventTransactionDepositTransferenciaService.apply( responseMsMono.map(item->{
                    Map<String, Object> map = (Map<String, Object>) item.getDinBody();
                    logger.info("Handler - SaveDepositarTRansferenciaUseCase - map {}", map);
                    BankTransactionDepositTransfer transaction = new BankTransactionDepositTransfer();

                    transaction.setCustomerSenderId( map.get("customerSenderId").toString());
                    transaction.setAccountNumberSender(map.get("accountNumberSender").toString());
                    transaction.setAmount(BigDecimal.valueOf(Double.parseDouble(map.get("amount").toString())));

                    transaction.setCustomerReceiverId( map.get("customerReceiverId").toString());
                    transaction.setAccountNumberReceiver(map.get("accountNumberReceiver").toString());


                    return transaction;
                })), Event.class));
    }


}
