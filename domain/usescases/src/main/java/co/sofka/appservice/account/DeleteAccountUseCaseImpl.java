package co.sofka.appservice.account;

import co.sofka.Account;
import co.sofka.in.account.DeleteAccountUseCase;
import co.sofka.out.AccountRepository;
import reactor.core.publisher.Mono;

public class DeleteAccountUseCaseImpl implements DeleteAccountUseCase {

    private final AccountRepository repository;

    public DeleteAccountUseCaseImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Account> apply(Account account) {
        return repository.deleteAccount(account);
    }

}
