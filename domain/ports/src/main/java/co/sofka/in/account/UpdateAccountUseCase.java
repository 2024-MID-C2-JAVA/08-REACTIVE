package co.sofka.in.account;

import co.sofka.Account;
import co.sofka.Transaction;
import reactor.core.publisher.Mono;

public interface UpdateAccountUseCase {
    Mono<Account>apply(Account account, Transaction transaction);
}
