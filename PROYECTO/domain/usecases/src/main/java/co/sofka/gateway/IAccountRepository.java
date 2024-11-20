package co.sofka.gateway;


import co.sofka.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IAccountRepository {

    Mono<Account> save(Account item);
    Mono<Account> findByNumber(String accountNumber);
    Mono<Account> findById(String id);
    Flux<Account> getAll();
}
