package co.sofka.usecase.appBank;




import co.sofka.LogEvent;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveLogTransactionDetailService {
    void save(LogEvent log);


}
