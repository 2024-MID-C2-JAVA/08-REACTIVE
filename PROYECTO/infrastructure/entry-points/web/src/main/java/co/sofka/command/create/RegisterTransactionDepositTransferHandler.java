package co.sofka.command.create;

import co.sofka.Account;
import co.sofka.LogEvent;
import co.sofka.Transaction;
import co.sofka.TransactionAccountDetail;
import co.sofka.command.dto.BankTransactionDepositTransfer;
import co.sofka.command.dto.request.RequestMs;
import co.sofka.command.dto.response.DinError;
import co.sofka.command.dto.response.ResponseMs;
import co.sofka.config.EncryptionAndDescryption;
import co.sofka.config.TokenByDinHeaders;
import co.sofka.gateway.ITransactionAccountDetailRepository;
import co.sofka.middleware.AccountNotExistException;
import co.sofka.middleware.AccountNotHaveBalanceException;
import co.sofka.middleware.ErrorDecryptingDataException;
import co.sofka.usecase.appBank.IGetAccountByNumberService;
import co.sofka.usecase.appBank.ISaveAccountService;
import co.sofka.usecase.appBank.ISaveLogTransactionDetailService;
import co.sofka.usecase.appBank.ISaveTransactionService;
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
public class RegisterTransactionDepositTransferHandler {

    private static final Logger logger = LoggerFactory.getLogger(RegisterTransactionDepositTransferHandler.class);

    private final IGetAccountByNumberService service;

    private final ISaveTransactionService saveTransactionService;

    private final ISaveAccountService saveAccountService;

    private final ITransactionAccountDetailRepository transactionAccountDetailRepository;

    private final TokenByDinHeaders utils;

    private EncryptionAndDescryption encryptionAndDescryption;

    private final ISaveLogTransactionDetailService saveLogTransactionDetailService;

    public Mono<ResponseMs<Transaction>> apply(RequestMs<BankTransactionDepositTransfer> request) {

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
        try {
            decode = encryptionAndDescryption.decryptAes(request.getDinBody().getAccountNumberSender(),vectorInicializacion,LlaveSimetrica);
        } catch (Exception e) {
            throw new AccountNotExistException("Error al desencriptar el numero de cuenta.", request.getDinHeader(),1001);
        }
        logger.info("Buscando Account por numero: "+decode);

        Account accountSend = service.findByNumber(decode).block();

        if (accountSend==null){
            throw new AccountNotExistException("La cuenta Origen no existe", request.getDinHeader(),1002);
        }

        String decodeReciver = "";
        try {
            decodeReciver = encryptionAndDescryption.decryptAes(request.getDinBody().getAccountNumberReceiver(),vectorInicializacion,LlaveSimetrica);
        } catch (Exception e) {
            throw new AccountNotExistException("Error al desencriptar el numero de cuenta.", request.getDinHeader(),1001);
        }
        logger.info("Buscando Account por numero: "+decodeReciver);
        Account accountReciver = service.findByNumber(decodeReciver).block();

        if (accountReciver==null){
            throw new AccountNotExistException("La cuenta Destino no existe", request.getDinHeader(),1002);
        }

        if (accountSend.getAmount().subtract(request.getDinBody().getAmount().add(new BigDecimal(1.5))).intValue() < 0) {
            throw new AccountNotHaveBalanceException("No tiene saldo suficiente.", request.getDinHeader(),1003);
        }

//        logger.info("Guardando Transaccion {}",accountReciver.getCustomer().getId());

        Transaction transaction = new Transaction();
        transaction.setTransactionCost(new BigDecimal(1.5));
        transaction.setAmountTransaction(request.getDinBody().getAmount());
        transaction.setTimeStamp(LocalDateTime.now());
        transaction.setTypeTransaction("Transferencia");

        //Transaction save = saveTransactionService.save(transaction);


        TransactionAccountDetail transactionAccountDetail = new TransactionAccountDetail();
        transactionAccountDetail.setAccount(accountSend);
        transactionAccountDetail.setTransaction(transaction);
        transactionAccountDetail.setTransactionRole("Payroll");

        transactionAccountDetailRepository.save(transactionAccountDetail);


        TransactionAccountDetail transactionAccountDetailCredit = new TransactionAccountDetail();
        transactionAccountDetailCredit.setAccount(accountReciver);
        transactionAccountDetailCredit.setTransaction(transaction);
        transactionAccountDetailCredit.setTransactionRole("Supplier");

        TransactionAccountDetail save =transactionAccountDetailRepository.save(transactionAccountDetailCredit).block();

        LogEvent logEvent = new LogEvent();
        logEvent.setId(UUID.randomUUID().toString());
        logEvent.setMessage(utils.encode(save.getTransaction().toString()));
        logEvent.setFecha(LocalDate.now().toString());
        logEvent.setType(transaction.getTypeTransaction());

        try {
            saveLogTransactionDetailService.save(logEvent);
        } catch (Exception e) {
           logger.error("Error al guardar el log {}",e.getMessage());
        }


        accountReciver.setAmount(accountReciver.getAmount().add(request.getDinBody().getAmount()));
        saveAccountService.save(accountReciver);

        accountSend.setAmount(accountSend.getAmount().subtract(request.getDinBody().getAmount().add(new BigDecimal(1.5))));
        saveAccountService.save(accountSend);


        responseMs.setDinBody(transaction);

        return Mono.just(responseMs);
    }
}
