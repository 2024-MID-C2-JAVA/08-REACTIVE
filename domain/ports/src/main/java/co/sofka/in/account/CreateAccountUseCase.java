package co.sofka.in.account;

import co.sofka.Account;
import reactor.core.publisher.Mono;

public interface CreateAccountUseCase {
    public Mono<Account>apply(Account account);
}
