package co.sofka.gateway;


import co.sofka.TransactionAccountDetail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITransactionAccountDetailRepository {

    Mono<TransactionAccountDetail> save(TransactionAccountDetail id);

    Flux<TransactionAccountDetail> getAll();

}
