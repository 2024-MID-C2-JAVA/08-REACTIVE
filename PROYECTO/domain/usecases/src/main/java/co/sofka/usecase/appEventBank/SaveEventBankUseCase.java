package co.sofka.usecase.appEventBank;



import co.sofka.Event;
import co.sofka.gateway.IEventBankRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class SaveEventBankUseCase implements ISaveEventBankService {

    private static final Logger logger = LoggerFactory.getLogger(SaveEventBankUseCase.class);

    private final IEventBankRepository repository;

    public SaveEventBankUseCase(IEventBankRepository repository) {
        this.repository = repository;
    }


    public Mono<Event> save(Mono<Event> event) {

        return repository.save(event.block());
    }


}
