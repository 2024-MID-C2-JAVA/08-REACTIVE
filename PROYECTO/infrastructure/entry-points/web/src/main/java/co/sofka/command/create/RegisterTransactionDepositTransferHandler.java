package co.sofka.command.create;

import co.sofka.Transaction;
import co.sofka.TransactionAccountDetail;
import co.sofka.config.EncryptionAndDescryption;
import co.sofka.config.TokenByDinHeaders;
import co.sofka.commands.request.BankTransactionDepositTransfer;
import co.sofka.middleware.ErrorDecryptingDataException;
import co.sofka.usecase.appBank.IGetCustomerByIdService;
import co.sofka.usecase.appBank.ISaveCustomerService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RegisterTransactionDepositTransferHandler {

    private static final Logger logger = LoggerFactory.getLogger(RegisterTransactionDepositTransferHandler.class);

    private final IGetCustomerByIdService getCustomerByIdService;

    private final ISaveCustomerService saveCustomerService;

    private final TokenByDinHeaders utils;

    private EncryptionAndDescryption encryptionAndDescryption;


    public void apply(BankTransactionDepositTransfer request) {

        String LlaveSimetrica = "";
        try{
            LlaveSimetrica = utils.decode(request.getLlaveSimetrica());
        } catch (Exception e) {
            throw new ErrorDecryptingDataException("Error al desencriptar la LlaveSimetrica.", 1001);
        }

        String vectorInicializacion = "";
        try{
            vectorInicializacion = utils.decode(request.getVectorInicializacion());
        } catch (Exception e) {
            throw new ErrorDecryptingDataException("Error al desencriptar la vectorInicializacion.", 1001);
        }

        final String sender = encryptionAndDescryption.decryptAes(request.getAccountNumberSender(),vectorInicializacion,LlaveSimetrica);

        logger.info("Account por numero: "+sender);

        final String resiver = encryptionAndDescryption.decryptAes(request.getAccountNumberReceiver(),vectorInicializacion,LlaveSimetrica);

        logger.info("Buscando Account por numero: "+resiver);

        logger.info("Buscando Customer por id Sender {}  Reciver {}", request.getCustomerSenderId(),request.getCustomerReceiverId());


        Transaction transaction = new Transaction();
        transaction.setTransactionCost(new BigDecimal(1.5));
        transaction.setAmountTransaction(request.getAmount());
        transaction.setTimeStamp(LocalDateTime.now());
        transaction.setTypeTransaction("Transferencia");

        getCustomerByIdService.findById(request.getCustomerSenderId())
                .map(customer -> {

                    logger.info("Guardando Transaction {}", customer.getAccounts().size());

                    customer.getAccounts().forEach(account -> {
                        if (account.getNumber().equals(sender)) {
                            TransactionAccountDetail transactionAccountDetail = new TransactionAccountDetail();
                            transactionAccountDetail.setId(UUID.randomUUID().toString());
                            transactionAccountDetail.setAccount(account);
                            transactionAccountDetail.setTransaction(transaction);
                            transactionAccountDetail.setTransactionRole("Payroll");

                            logger.info("Guardando TransactionAccountDetail Sender");
                            if (account.getTransactionAccountDetails() == null) {
                                account.setTransactionAccountDetails(new ArrayList<>());
                            }
                            account.getTransactionAccountDetails().add(transactionAccountDetail);
                            logger.info("Guardando Update Salda Account");
                            account.setAmount(account.getAmount().subtract(request.getAmount().add(transaction.getTransactionCost())));
                            saveCustomerService.apply(customer);
                        }
                    });

                    return transaction;

                })
                .subscribe();

        getCustomerByIdService.findById(request.getCustomerReceiverId())
                .map(customer -> {

                    logger.info("Guardando Transaction {}", customer.getAccounts().size());

                    customer.getAccounts().forEach(account -> {
                        if (account.getNumber().equals(resiver)) {
                            TransactionAccountDetail transactionAccountDetail = new TransactionAccountDetail();
                            transactionAccountDetail.setId(UUID.randomUUID().toString());
                            transactionAccountDetail.setAccount(account);
                            transactionAccountDetail.setTransaction(transaction);
                            transactionAccountDetail.setTransactionRole("Supplier");

                            logger.info("Guardando TransactionAccountDetail Receiver");
                            if (account.getTransactionAccountDetails() == null) {
                                account.setTransactionAccountDetails(new ArrayList<>());
                            }
                            account.getTransactionAccountDetails().add(transactionAccountDetail);
                            logger.info("Guardando Update Salda Account");
                            account.setAmount(account.getAmount().add(request.getAmount()));
                            saveCustomerService.apply(customer);
                        }
                    });

                    return transaction;

                })
                .subscribe();

    }
}
