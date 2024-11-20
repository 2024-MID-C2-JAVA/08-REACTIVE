package co.sofka.usecase.appBank;




import co.sofka.Customer;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveCustomerService {
    Mono<Customer> save(Customer item);


}
