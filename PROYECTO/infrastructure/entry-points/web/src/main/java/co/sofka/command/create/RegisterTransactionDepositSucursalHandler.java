package co.sofka.command.create;

import co.sofka.Account;
import co.sofka.LogEvent;
import co.sofka.Transaction;
import co.sofka.TransactionAccountDetail;
import co.sofka.command.dto.BankTransactionDepositSucursal;
import co.sofka.command.dto.request.RequestMs;
import co.sofka.command.dto.response.DinError;
import co.sofka.command.dto.response.ResponseMs;
import co.sofka.config.EncryptionAndDescryption;
import co.sofka.config.TokenByDinHeaders;
import co.sofka.gateway.ITransactionAccountDetailRepository;
import co.sofka.middleware.AccountNotExistException;
import co.sofka.middleware.ErrorDecryptingDataException;
import co.sofka.usecase.appBank.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RegisterTransactionDepositSucursalHandler {

    private static final Logger logger = LoggerFactory.getLogger(RegisterTransactionDepositSucursalHandler.class);

    private final IGetAccountByNumberService service;

    private final ISaveTransactionService saveTransactionService;

    private final ISaveAccountService saveAccountService;

    private final ITransactionAccountDetailRepository transactionAccountDetailRepository;

    private final ISaveTransactionAccountDetailService saveTransactionAccountDetailService;

    private final ISaveLogTransactionDetailService saveLogTransactionDetailService;

    private final TokenByDinHeaders utils;

    private EncryptionAndDescryption encryptionAndDescryption;

    public Mono<ResponseMs<Transaction>> apply(RequestMs<BankTransactionDepositSucursal> request) {

        ResponseMs<Transaction> responseMs = new ResponseMs<>();
        responseMs.setDinHeader(request.getDinHeader());
        DinError error = new DinError();
        responseMs.setDinError(error);

        String LlaveSimetrica = "";
        try{
            LlaveSimetrica = utils.decode(request.getDinHeader().getLlaveSimetrica());
        } catch (Exception e) {
            throw new ErrorDecryptingDataException("Error al desencriptar la LlaveSimetrica.", request.getDinHeader(),1001);
        }

        String vectorInicializacion = "";
        try{
            vectorInicializacion = utils.decode(request.getDinHeader().getVectorInicializacion());
        } catch (Exception e) {
            throw new ErrorDecryptingDataException("Error al desencriptar la vectorInicializacion.", request.getDinHeader(),1001);
        }




        logger.info("Buscando Account por numero");
        String decode = "";
        try{
            decode = encryptionAndDescryption.decryptAes(request.getDinBody().getAccountNumberClient(),vectorInicializacion,LlaveSimetrica);
        } catch (Exception e) {
            throw new ErrorDecryptingDataException("Error al desencriptar el numero de cuenta.", request.getDinHeader(),1001);
        }
        logger.info("Buscando Account por numero: "+decode);

        Account account = service.findByNumber(decode).block();

        if (account==null){
            throw new AccountNotExistException("La cuenta no existe", request.getDinHeader(),1002);
        }


        Transaction transaction = new Transaction();
        transaction.setTransactionCost(new BigDecimal(0));
        transaction.setAmountTransaction(request.getDinBody().getAmount());
        transaction.setTimeStamp(LocalDateTime.now());
        transaction.setTypeTransaction("Deposito Sucursal");

        //Transaction save = saveTransactionService.save(transaction);


        TransactionAccountDetail transactionAccountDetail = new TransactionAccountDetail();
        transactionAccountDetail.setAccount(account);
        transactionAccountDetail.setTransaction(transaction);
        transactionAccountDetail.setTransactionRole("Supplier");

        TransactionAccountDetail save = saveTransactionAccountDetailService.save(transactionAccountDetail).block();

        LogEvent logEvent = new LogEvent();
        logEvent.setId(UUID.randomUUID().toString());
        logEvent.setMessage(utils.encode(save.getTransaction().toString()));
        logEvent.setFecha(LocalDate.now().toString());
        logEvent.setType(transaction.getTypeTransaction());

        try {
            saveLogTransactionDetailService.save(logEvent);
        } catch (Exception e) {
            logger.error("Error al guardar el log {}",e.getMessage());
        }        //transactionAccountDetailRepository.save(transactionAccountDetail);

        account.setAmount(account.getAmount().add(request.getDinBody().getAmount()));
        saveAccountService.save(account);

        responseMs.setDinBody(transaction);

        return Mono.just(responseMs);
    }
}
