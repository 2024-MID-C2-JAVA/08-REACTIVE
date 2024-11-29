package co.sofka.usecase.appBank;




import co.sofka.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IGetCustomerByIdService {
    Mono<Customer> findById(String id);

}
