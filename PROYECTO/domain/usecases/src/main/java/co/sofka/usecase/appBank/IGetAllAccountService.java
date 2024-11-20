package co.sofka.usecase.appBank;




import co.sofka.Account;
import reactor.core.publisher.Flux;

import java.util.List;

@FunctionalInterface
public interface IGetAllAccountService {
    Flux<Account> getAll();


}
