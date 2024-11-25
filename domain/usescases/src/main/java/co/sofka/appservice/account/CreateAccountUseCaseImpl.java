package co.sofka.appservice.account;

import co.sofka.Account;
import co.sofka.events.AccountCreatedEvent;
import co.sofka.in.account.CreateAccountUseCase;
import co.sofka.out.AccountRepository;
import co.sofka.rabbitMq.CreateAccountEventUseCase;
import reactor.core.publisher.Mono;


public class CreateAccountUseCaseImpl implements CreateAccountUseCase {

    private final AccountRepository repository;

    public CreateAccountUseCaseImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Account> apply(Account account) {
        return repository.createAccount(account);
    }

}
