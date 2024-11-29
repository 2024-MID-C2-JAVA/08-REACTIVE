package co.sofka.command.create;

import co.sofka.Transaction;
import co.sofka.TransactionAccountDetail;
import co.sofka.config.EncryptionAndDescryption;
import co.sofka.config.TokenByDinHeaders;
import co.sofka.commands.request.BankTransactionBuys;
import co.sofka.middleware.ErrorDecryptingDataException;
import co.sofka.usecase.appBank.*;
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
public class RegisterTransactionCompraHandler {

    private static final Logger logger = LoggerFactory.getLogger(RegisterTransactionCompraHandler.class);

    private final IGetCustomerByIdService getCustomerByIdService;

    private final ISaveCustomerService saveCustomerService;

    private final TokenByDinHeaders utils;

    private EncryptionAndDescryption encryptionAndDescryption;




    public void apply(BankTransactionBuys request) {

        String LlaveSimetrica = "";
        try {
            LlaveSimetrica = utils.decode(request.getLlaveSimetrica());
        } catch (Exception e) {
            throw new ErrorDecryptingDataException("Error al desencriptar la LlaveSimetrica.", 1001);
        }

        String vectorInicializacion = "";
        try {
            vectorInicializacion = utils.decode(request.getVectorInicializacion());
        } catch (Exception e) {
            throw new ErrorDecryptingDataException("Error al desencriptar la vectorInicializacion.", 1001);
        }

        logger.info("Buscando Account por numero");
        final String decode = encryptionAndDescryption.decryptAes(request.getAccountNumberClient(), vectorInicializacion, LlaveSimetrica);

        logger.info("Buscando Account por numero: " + decode);

        logger.info("Buscando Customer por id {}", request.getCustomerId());


        getCustomerByIdService.findById(request.getCustomerId())
                .map(customer -> {
                    logger.info("Customer encontrado {}", customer);
                    Transaction transaction = new Transaction();
                    transaction.setId(UUID.randomUUID().toString());
                    transaction.setTransactionCost(new BigDecimal(1));
                    transaction.setAmountTransaction(request.getAmount());
                    transaction.setTimeStamp(LocalDateTime.now());
                    transaction.setTypeTransaction("Compra");

                    if (request.getTypeBuys()==0) {
                        transaction.setTransactionCost(new BigDecimal(0));
                    }
                    else{
                        transaction.setTransactionCost(new BigDecimal(5));
                    }



                    logger.info("Guardando Transaction");

                    logger.info("Guardando Transaction {}", customer.getAccounts().size());

                    customer.getAccounts().forEach(account -> {
                        if (account.getNumber().equals(decode)) {
                            TransactionAccountDetail transactionAccountDetail = new TransactionAccountDetail();
                            transactionAccountDetail.setId(UUID.randomUUID().toString());
                            transactionAccountDetail.setAccount(account);
                            transactionAccountDetail.setTransaction(transaction);
                            transactionAccountDetail.setTransactionRole("Payroll");

                            logger.info("Guardando TransactionAccountDetail");
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
    }
}
