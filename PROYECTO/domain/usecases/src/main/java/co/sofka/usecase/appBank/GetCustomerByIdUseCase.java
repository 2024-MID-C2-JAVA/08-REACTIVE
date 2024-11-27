package co.sofka.usecase.appBank;



import co.sofka.Customer;
import co.sofka.gateway.ICustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GetCustomerByIdUseCase implements IGetCustomerByIdService {

    private static final Logger logger = LoggerFactory.getLogger(GetCustomerByIdUseCase.class);

    private final ICustomerRepository repository;

    public GetCustomerByIdUseCase(ICustomerRepository repository) {
        this.repository = repository;
    }


    @Override
    public Mono<Customer> findById(String id) {
        logger.info("GetCustomerByIdUseCase: findById");
        return repository.findById(id);
    }
}
