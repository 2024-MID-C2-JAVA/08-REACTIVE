package co.sofka.appservice.account;

import co.sofka.Account;
import co.sofka.Transaction;
import co.sofka.appservice.account.strategy.AccountUpdateContext;
import co.sofka.in.account.UpdateAccountUseCase;
import co.sofka.out.AccountRepository;
import reactor.core.publisher.Mono;

public class UpdateAccountUseCaseImpl implements UpdateAccountUseCase {

    private final AccountRepository repository;

    public UpdateAccountUseCaseImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Account> apply(Account account, Transaction transaction) {
        Account account1= AccountUpdateContext.accountUpdate(transaction).update(transaction,account);
        return repository.updateAccount(account1);
    }

}
