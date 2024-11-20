package co.sofka.out;

import co.sofka.Account;
import reactor.core.publisher.Mono;

public interface AccountRepository {
    Mono<Account>createAccount(Account account);
    Mono<Account>deleteAccount(Account account);
    Mono<Account>updateAccount(Account account);
    Mono<Account>getAccount(Account account);
}
