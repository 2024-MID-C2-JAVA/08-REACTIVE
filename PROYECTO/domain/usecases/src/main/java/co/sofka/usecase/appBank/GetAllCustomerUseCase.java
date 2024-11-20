package co.sofka.usecase.appBank;



import co.sofka.Customer;
import co.sofka.gateway.ICustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.List;

public class GetAllCustomerUseCase implements IGetAllCustomerService {

    private static final Logger logger = LoggerFactory.getLogger(GetAllCustomerUseCase.class);

    private final ICustomerRepository repository;

    public GetAllCustomerUseCase(ICustomerRepository repository) {
        this.repository = repository;
    }


    public Flux<Customer> getAll() {
        return repository.getAll();
    }


}
