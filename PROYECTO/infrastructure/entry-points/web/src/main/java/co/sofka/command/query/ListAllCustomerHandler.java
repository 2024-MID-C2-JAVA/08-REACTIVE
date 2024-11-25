package co.sofka.command.query;

import co.sofka.Customer;
import co.sofka.command.dto.AccountDTO;
import co.sofka.command.dto.CustomerDTO;
import co.sofka.command.dto.request.RequestMs;
import co.sofka.command.dto.response.DinError;
import co.sofka.command.dto.response.ResponseMs;
import co.sofka.config.EncryptionAndDescryption;
import co.sofka.config.TokenByDinHeaders;
import co.sofka.middleware.ErrorDecryptingDataException;
import co.sofka.usecase.appBank.IGetAllCustomerService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ListAllCustomerHandler {

    private static final Logger logger = LoggerFactory.getLogger(ListAllCustomerHandler.class);

    private final IGetAllCustomerService service;

    private final TokenByDinHeaders utils;

    private EncryptionAndDescryption encryptionAndDescryption;

    public Mono<ResponseMs<List<CustomerDTO>>> getAll(RequestMs<Void> request) {

        ResponseMs<List<CustomerDTO>> responseMs = new ResponseMs<>();
        responseMs.setDinHeader(request.getDinHeader());
        DinError error = new DinError();


        Mono<List<CustomerDTO>> listMono = service.getAll().map(customer -> {
            logger.info("Customer: " + customer.getUsername());

            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setUsername(customer.getUsername());
            customerDTO.setRol(customer.getRol());
            customerDTO.setId(customer.getId());

            if (customer.getAccounts() != null && !customer.getAccounts().isEmpty()) {
                List<AccountDTO> list = customer.getAccounts().stream().map(account1 -> {
                    AccountDTO accountDTO = new AccountDTO();
                    logger.info("Account number: " + account1.getNumber());

                    String LlaveSimetrica = "";
                    try {
                        LlaveSimetrica = utils.decode(request.getDinHeader().getLlaveSimetrica());
                    } catch (Exception e) {
                        throw new ErrorDecryptingDataException("Error al desencriptar la LlaveSimetrica.",  1001);
                    }

                    String vectorInicializacion = "";
                    try {
                        vectorInicializacion = utils.decode(request.getDinHeader().getVectorInicializacion());
                    } catch (Exception e) {
                        throw new ErrorDecryptingDataException("Error al desencriptar la vectorInicializacion.",  1001);
                    }
                    accountDTO.setId(account1.getId());
                    accountDTO.setNumber(encryptionAndDescryption.encriptAes(account1.getNumber(), vectorInicializacion, LlaveSimetrica));
                    accountDTO.setAmount(account1.getAmount());
                    return accountDTO;
                }).toList();
                customerDTO.setAccounts(list);
            }


            return customerDTO;
        }).collectList();


        Mono<ResponseMs<List<CustomerDTO>>> responseMsMono = listMono.flatMap(list -> {
            responseMs.setDinBody(list);
            return Mono.just(responseMs);
        });



        return responseMsMono;
    }
}
