package co.sofka.handler;

import co.sofka.Account;
import co.sofka.data.account.AccountDto;
import co.sofka.in.account.CreateAccountUseCase;
import co.sofka.in.account.DeleteAccountUseCase;
import co.sofka.in.account.GetAccountByIdUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
public class AccountHandler {

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;

    public AccountHandler(CreateAccountUseCase createAccountUseCase, GetAccountByIdUseCase getAccountByIdUseCase, DeleteAccountUseCase deleteAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
    }

    public Mono<Account> createAccount(AccountDto accountDTO) {
            Account account = new Account();
            account.setNumber(Integer.parseInt(accountDTO.getNumber()));
            account.setAmount(accountDTO.getAmount());
            account.setCustomerId(accountDTO.getCustomerId());
            account.setCreatedAt(LocalDate.now());
            return createAccountUseCase.apply(account);
    }

    public Mono<Account>deleteAccount(AccountDto accountDTO) {
        Account account = new Account();
        account.setId(accountDTO.getId());
        return deleteAccountUseCase.apply(account);
    }

    public Mono<Account>getAccountById(AccountDto accountDTO) {
        return getAccountByIdUseCase.apply(new Account(accountDTO.getId()));
    }
}
