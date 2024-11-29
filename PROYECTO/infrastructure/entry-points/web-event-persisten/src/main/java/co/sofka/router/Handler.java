package co.sofka.router;

import co.sofka.command.dto.DinHeader;
import co.sofka.command.dto.request.RequestMs;
import co.sofka.command.dto.response.ResponseMs;
import co.sofka.commands.request.*;
import co.sofka.generic.DomainEvent;
import co.sofka.usecase.appEventBank.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Handler {

    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    private  final SaveEventCustomerUseCase saveCustomerUseCase;

    private  final SaveEventAccountUseCase saveCustomerAccountUseCase;
//
    private final ISaveEventTransactionDepositSucursalService saveEventTransactionDepositSucursalService;

    private final ISaveEventTransactionDepositCajeroService saveEventTransactionDepositCajeroService;

    private final ISaveEventTransactionDepositTransferenciaService saveEventTransactionDepositTransferenciaService;

    private final ISaveEventTransactionRetiroCajeroService saveEventTransactionRetiroCajeroUseCase;

    private final SaveEventTransactionCompraUseCase saveEventTransactionCompraUseCase;



    @SneakyThrows
    public Mono<ServerResponse> SaveCustomerUseCase(ServerRequest serverRequest) {
        logger.info("Handler - SaveCustomerUseCase");
        Mono<RequestMs> responseMsMono = serverRequest.bodyToMono(RequestMs.class);

//        String username = webClient.post("http://localhost:8088", "username", List.of());
//
//        logger.info("Service {}",username);

        Mono<ResponseMs> response = responseMsMono.map(item -> {
            ResponseMs responseMs = new ResponseMs();
            responseMs.setDinHeader(item.getDinHeader());
            return responseMs;
        });


        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(saveCustomerUseCase.apply( responseMsMono.map(item->{
                    Map<String, Object> map = (Map<String, Object>) item.getDinBody();
                    logger.info("Handler - SaveCustomerUseCase - map {}", map);

                    CustomerCommand customer = new CustomerCommand();
                    customer.setUserName( map.get("username").toString());
                    customer.setPwd(map.get("pwd").toString());
                    customer.setRol(map.get("rol").toString());
                    return customer;
                })), DomainEvent.class));
    }


    @SneakyThrows
    public Mono<ServerResponse> SaveCustomerAccountUseCase(ServerRequest serverRequest) {
        logger.info("Handler - SaveCustomerAccountUseCase");
        Mono<RequestMs> responseMsMono = serverRequest.bodyToMono(RequestMs.class);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(saveCustomerAccountUseCase.apply( responseMsMono.map(item->{
                    Map<String, Object> map = (Map<String, Object>) item.getDinBody();
                    logger.info("Handler - SaveCustomerAccountUseCase - map {}", map);

                    AccountCommand customer = new AccountCommand();
                    customer.setCustomerId( map.get("customerId").toString());
                    customer.setAccountId(map.get("accountId").toString());
                    customer.setNumber(map.get("number").toString());
                    customer.setAmount(BigDecimal.valueOf(Long.parseLong(map.get("amount").toString())));
                    return customer;
                })), DomainEvent.class));
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
                    transaction.setCustumerTransactionSenderId(map.get("custumerTransactionSenderId").toString());
                    transaction.setCustumerTransactionResiverId(map.get("custumerTransactionResiverId").toString());
                    transaction.setNumberAccountTransactionSenderId(map.get("numberAccountTransactionSenderId").toString());
                    transaction.setNumberAccountTransactionResiverId(map.get("numberAccountTransactionResiverId").toString());

                    transaction.setAmountTransaction(BigDecimal.valueOf(Double.parseDouble(map.get("amountTransaction").toString())));

                    DinHeader dinHeader = item.getDinHeader();
                    logger.info("Handler - SaveCustomerUseCase - dinHeader {}", dinHeader);
                    transaction.setLlaveSimetrica(dinHeader.getLlaveSimetrica());
                    transaction.setVectorInicializacion(dinHeader.getVectorInicializacion());

                    return transaction;
                })), DomainEvent.class));
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
                    transaction.setCustumerTransactionSenderId(map.get("custumerTransactionSenderId").toString());
                    transaction.setCustumerTransactionResiverId(map.get("custumerTransactionResiverId").toString());
                    transaction.setNumberAccountTransactionSenderId(map.get("numberAccountTransactionSenderId").toString());
                    transaction.setNumberAccountTransactionResiverId(map.get("numberAccountTransactionResiverId").toString());

                    transaction.setAmountTransaction(BigDecimal.valueOf(Double.parseDouble(map.get("amountTransaction").toString())));

                    DinHeader dinHeader = item.getDinHeader();
                    logger.info("Handler - SaveCustomerUseCase - dinHeader {}", dinHeader);
                    transaction.setLlaveSimetrica(dinHeader.getLlaveSimetrica());
                    transaction.setVectorInicializacion(dinHeader.getVectorInicializacion());


                    return transaction;
                })), DomainEvent.class));
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

                    transaction.setCustomerId( map.get("customerId").toString());
                    transaction.setCustumerTransactionSenderId(map.get("custumerTransactionSenderId").toString());
                    transaction.setCustumerTransactionResiverId(map.get("custumerTransactionResiverId").toString());
                    transaction.setNumberAccountTransactionSenderId(map.get("numberAccountTransactionSenderId").toString());
                    transaction.setNumberAccountTransactionResiverId(map.get("numberAccountTransactionResiverId").toString());

                    transaction.setAmountTransaction(BigDecimal.valueOf(Double.parseDouble(map.get("amountTransaction").toString())));

                    DinHeader dinHeader = item.getDinHeader();
                    logger.info("Handler - SaveCustomerUseCase - dinHeader {}", dinHeader);
                    transaction.setLlaveSimetrica(dinHeader.getLlaveSimetrica());
                    transaction.setVectorInicializacion(dinHeader.getVectorInicializacion());


                    return transaction;
                })), DomainEvent.class));
    }


    public Mono<ServerResponse> SaveRetiroCajeroUseCase(ServerRequest serverRequest) {
        logger.info("Handler - SaveRetiroCajeroUseCase");
        Mono<RequestMs> responseMsMono = serverRequest.bodyToMono(RequestMs.class);


        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(saveEventTransactionRetiroCajeroUseCase.apply( responseMsMono.map(item->{
                    Map<String, Object> map = (Map<String, Object>) item.getDinBody();
                    logger.info("Handler - SaveRetiroCajeroUseCase - map {}", map);
                    BankTransactionWithdrawFromATM transaction = new BankTransactionWithdrawFromATM();

                    transaction.setCustomerId( map.get("customerId").toString());
                    transaction.setCustumerTransactionSenderId(map.get("custumerTransactionSenderId").toString());
                    transaction.setCustumerTransactionResiverId(map.get("custumerTransactionResiverId").toString());
                    transaction.setNumberAccountTransactionSenderId(map.get("numberAccountTransactionSenderId").toString());
                    transaction.setNumberAccountTransactionResiverId(map.get("numberAccountTransactionResiverId").toString());

                    transaction.setAmountTransaction(BigDecimal.valueOf(Double.parseDouble(map.get("amountTransaction").toString())));

                    DinHeader dinHeader = item.getDinHeader();
                    logger.info("Handler - SaveCustomerUseCase - dinHeader {}", dinHeader);
                    transaction.setLlaveSimetrica(dinHeader.getLlaveSimetrica());
                    transaction.setVectorInicializacion(dinHeader.getVectorInicializacion());

                    return transaction;
                })), DomainEvent.class));
    }


    public Mono<ServerResponse> SaveCompraUseCase(ServerRequest serverRequest) {
        logger.info("Handler - SaveCompraUseCase");
        Mono<RequestMs> responseMsMono = serverRequest.bodyToMono(RequestMs.class);


        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(saveEventTransactionCompraUseCase.apply( responseMsMono.map(item->{
                    Map<String, Object> map = (Map<String, Object>) item.getDinBody();
                    logger.info("Handler - SaveCompraUseCase - map {}", map);
                    BankTransactionBuys transaction = new BankTransactionBuys();

                    transaction.setCustomerId( map.get("customerId").toString());
                    transaction.setCustumerTransactionSenderId(map.get("custumerTransactionSenderId").toString());
                    transaction.setCustumerTransactionResiverId(map.get("custumerTransactionResiverId").toString());
                    transaction.setNumberAccountTransactionSenderId(map.get("numberAccountTransactionSenderId").toString());
                    transaction.setNumberAccountTransactionResiverId(map.get("numberAccountTransactionResiverId").toString());
                    transaction.setTypeBuys(Integer.parseInt(map.get("typeBuys").toString()));

                    transaction.setAmountTransaction(BigDecimal.valueOf(Double.parseDouble(map.get("amountTransaction").toString())));

                    DinHeader dinHeader = item.getDinHeader();
                    logger.info("Handler - SaveCustomerUseCase - dinHeader {}", dinHeader);
                    transaction.setLlaveSimetrica(dinHeader.getLlaveSimetrica());
                    transaction.setVectorInicializacion(dinHeader.getVectorInicializacion());

                    return transaction;
                })), DomainEvent.class));
    }



}
