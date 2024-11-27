package co.sofka.usecase.appBank;




import co.sofka.Customer;
import reactor.core.publisher.Mono;

import java.util.List;

@FunctionalInterface
public interface IGetCustomerByUserNameService {
    Mono<Customer> findByUsername(String username);

}
