package co.sofka.appservice.account;

import co.sofka.Account;
import co.sofka.out.AccountViewsRepository;
import reactor.core.publisher.Mono;

public class GetAccountByIdUseCaseView implements co.sofka.in.account.GetAccountByIdUseCase {

    private final AccountViewsRepository repository;

    public GetAccountByIdUseCaseView(AccountViewsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Account> apply(Account account) {
        return repository.getAccount(account);
    }

}
