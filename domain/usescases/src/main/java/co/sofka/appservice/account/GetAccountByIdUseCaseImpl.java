package co.sofka.appservice.account;

import co.sofka.Account;
import co.sofka.in.account.GetAccountByIdUseCase;
import co.sofka.out.AccountRepository;
import reactor.core.publisher.Mono;

public class GetAccountByIdUseCaseImpl implements GetAccountByIdUseCase{

    private final AccountRepository repository;

    public GetAccountByIdUseCaseImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Account> apply(Account account) {
        return repository.getAccount(account);
    }

}
