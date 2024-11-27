package co.sofka.usecase.appBank;




import co.sofka.Account;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveAccountService {
    Mono<Account> save(Account item);


}
