package co.sofka.usecase.appBank;




import co.sofka.TransactionAccountDetail;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveTransactionAccountDetailService {
    Mono<TransactionAccountDetail> save(TransactionAccountDetail item);


}
