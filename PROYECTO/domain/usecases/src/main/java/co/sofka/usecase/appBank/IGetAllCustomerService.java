package co.sofka.usecase.appBank;




import co.sofka.Customer;
import reactor.core.publisher.Flux;

import java.util.List;

@FunctionalInterface
public interface IGetAllCustomerService {
    Flux<Customer> getAll();

}
