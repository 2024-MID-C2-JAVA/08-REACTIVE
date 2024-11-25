package co.sofka.out;

import co.sofka.Account;
import reactor.core.publisher.Mono;

public interface AccountViewsRepository {
    Mono<Account>getAccount(Account account);
}
