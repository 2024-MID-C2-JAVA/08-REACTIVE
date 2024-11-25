package co.sofka.usecase.appBank;


import co.sofka.Customer;
import co.sofka.gateway.ICustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class SaveCustomerUseCase implements ISaveCustomerService {

    private static final Logger logger = LoggerFactory.getLogger(SaveCustomerUseCase.class);

    private final ICustomerRepository repository;


    public SaveCustomerUseCase(ICustomerRepository repository) {
        this.repository = repository;
    }


    @Override
    public Flux<Customer> apply(Customer item) {

       return repository.save(item).flux();


    }
}
