package co.sofka.command.create;

import co.sofka.Account;
import co.sofka.Customer;
import co.sofka.command.dto.AccountDTO;
import co.sofka.command.dto.request.CustomerSaveDTO;
import co.sofka.config.EncryptionAndDescryption;
import co.sofka.config.TokenByDinHeaders;
import co.sofka.security.configuration.jwt.JwtService;
import co.sofka.usecase.appBank.IGetAllCustomerService;
import co.sofka.usecase.appBank.ISaveCustomerService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class SaveCustumerHandler {

    private static final Logger logger = LoggerFactory.getLogger(SaveCustumerHandler.class);

    private final ISaveCustomerService service;

    private final TokenByDinHeaders utils;

    private final JwtService jwtService;

    private EncryptionAndDescryption encryptionAndDescryption;

    private final IGetAllCustomerService serviceAll;


    public Flux<Customer> save(CustomerSaveDTO request) {


        Customer customer = new Customer();
        customer.setUsername(request.getUsername());
        customer.setPwd(jwtService.encryptionPassword(request.getPwd()));
        customer.setId(request.getId());
        customer.setRol(request.getRol());


        if (request.getAccounts() != null && !request.getAccounts().isEmpty()) {
            List<AccountDTO> accounts = request.getAccounts();
            if (accounts != null && !accounts.isEmpty()) {
                List<Account> accountsAll = new ArrayList<>();
               accounts.forEach(accountDTO -> {
                   Account accountDTO1 = new Account();
                   accountDTO1.setNumber(accountDTO.getNumber());
                   accountDTO1.setAmount(accountDTO.getAmount());
                   accountDTO1.setCreatedAt(LocalDate.now());
                   accountDTO1.setDeleted(false);
                   accountsAll.add(accountDTO1);
               });
                customer.setAccounts(accountsAll);
            }

        }

        logger.info("Customer to save: {}", customer);

        return service.apply(customer);
    }
}
