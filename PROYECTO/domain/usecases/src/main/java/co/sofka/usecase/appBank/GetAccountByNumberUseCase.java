package co.sofka.usecase.appBank;



import co.sofka.Account;
import co.sofka.gateway.IAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class GetAccountByNumberUseCase implements IGetAccountByNumberService {

    private static final Logger logger = LoggerFactory.getLogger(GetAccountByNumberUseCase.class);

    private final IAccountRepository repository;

    public GetAccountByNumberUseCase(IAccountRepository repository) {
        this.repository = repository;
    }


    @Override
    public Mono<Account> findByNumber(String number) {
        return repository.findByNumber(number);
    }
}
