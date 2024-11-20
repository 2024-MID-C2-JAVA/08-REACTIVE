package co.sofka.usecase.appBank;




import co.sofka.Account;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IGetAccountByNumberService {
    Mono<Account> findByNumber(String number);

}
