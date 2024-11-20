package co.sofka;

import co.sofka.data.transaction.ResponseTransactionMs;
import co.sofka.data.transaction.TransactionDto;
import co.sofka.handler.TransactionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transaction")
public class TransactionRestController {

    private final TransactionHandler transactionHandler;;

    public TransactionRestController(TransactionHandler transactionHandler) {
        this.transactionHandler = transactionHandler;
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<ResponseTransactionMs>> createTransaction(@RequestBody RequestMs<TransactionDto> dto) {
        return transactionHandler.createTransaction(dto.getDinBody())
                .then(Mono.just(ResponseEntity.ok(
                        new ResponseTransactionMs(dto.getDinHeader(), dto.getDinBody(),
                                new DinError(DinErrorEnum.TRANSACTION_CREATE)))))
                .onErrorResume(e -> Mono.just(ResponseEntity.ok(
                        new ResponseTransactionMs(dto.getDinHeader(), dto.getDinBody(),
                                new DinError(DinErrorEnum.TRANSACTION_ERROR)))));
    }
}
