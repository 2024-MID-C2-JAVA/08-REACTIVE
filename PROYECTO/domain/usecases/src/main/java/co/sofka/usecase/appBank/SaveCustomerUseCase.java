package co.sofka.usecase.appBank;



import co.sofka.Customer;
import co.sofka.gateway.ICustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class SaveCustomerUseCase implements ISaveCustomerService {

    private static final Logger logger = LoggerFactory.getLogger(SaveCustomerUseCase.class);

    private final ICustomerRepository repository;

    public SaveCustomerUseCase(ICustomerRepository repository) {
        this.repository = repository;
    }


    public Mono<Customer> save(Customer transaction) {

        return repository.save(transaction);
    }


}
