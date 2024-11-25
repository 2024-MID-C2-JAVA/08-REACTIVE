package co.sofka.usecase.appBank;



import co.sofka.Customer;
import co.sofka.gateway.ICustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class GetCustomerByUserNameUseCase implements IGetCustomerByUserNameService {

    private static final Logger logger = LoggerFactory.getLogger(GetCustomerByUserNameUseCase.class);

    private final ICustomerRepository repository;

    public GetCustomerByUserNameUseCase(ICustomerRepository repository) {
        this.repository = repository;
    }


    @Override
    public Mono<Customer> findByUsername(String username) {
        return repository.findByUsername(username);
    }
}
