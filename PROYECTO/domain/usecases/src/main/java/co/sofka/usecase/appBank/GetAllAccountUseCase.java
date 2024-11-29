package co.sofka.usecase.appBank;



import co.sofka.Account;
import co.sofka.gateway.IAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.List;

public class GetAllAccountUseCase implements IGetAllAccountService {

    private static final Logger logger = LoggerFactory.getLogger(GetAllAccountUseCase.class);

    private final IAccountRepository repository;

    public GetAllAccountUseCase(IAccountRepository repository) {
        this.repository = repository;
    }


    public Flux<Account> getAll() {
        return repository.getAll();
    }


}
