package co.sofka;

import co.sofka.handler.TransactionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionRestController {

    private final TransactionHandler transactionHandler;;

    public TransactionRestController(TransactionHandler transactionHandler) {
        this.transactionHandler = transactionHandler;
    }

//    @PostMapping("/create")
//    public Mono<ResponseEntity<ResponseTransactionMs>> createTransaction(@RequestBody RequestMs<TransactionDto> dto) {
//        return transactionHandler.createTransaction(dto.getDinBody()).flatMap(transaction -> {
//            TransactionDto transactionDto = new TransactionDto();
//            transactionDto.setId(transaction.getId());
//            transactionDto.setAmount(transaction.getAmount());
//            ResponseTransactionMs ms=new ResponseTransactionMs(dto.getDinHeader(),transactionDto,new DinError(DinErrorEnum.SUCCESS));
//            return Mono.just(ResponseEntity.ok().body(ms));
//        });
//    }
}
