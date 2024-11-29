package co.sofka.appservice.account;

import co.sofka.Account;
import co.sofka.in.account.GetAccountByIdUseCase;
import co.sofka.out.AccountViewsRepository;
import reactor.core.publisher.Mono;

public class GetAccountByIdUseCaseImpl implements GetAccountByIdUseCase {

    private final AccountViewsRepository repository;

    public GetAccountByIdUseCaseImpl(AccountViewsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Account> apply(Account account) {
        return repository.getAccount(account);
    }

}
