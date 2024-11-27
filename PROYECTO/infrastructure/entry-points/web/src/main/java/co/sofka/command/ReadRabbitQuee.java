package co.sofka.command;

import co.sofka.command.create.*;
import co.sofka.command.dto.request.CustomerSaveDTO;
import co.sofka.dto.*;
import co.sofka.event.Notification;
import co.sofka.utils.JsonToObjectConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class ReadRabbitQuee {

    private static final Logger logger = LoggerFactory.getLogger(ReadRabbitQuee.class);

   private final SaveCustumerHandler saveCustumerHandler;

   private final RegisterTransactionDepositSucursalHandler registerTransactionDepositSucursalHandler;

   private final RegisterTransactionDepositCajeroHandler registerTransactionDepositCajeroHandler;

   private final RegisterTransactionDepositTransferHandler registerTransactionDepositTransferHandler;

   private final RegisterTransactionWithDrawFromATMHandler registerTransactionWithDrawFromATMHandler;

   private final RegisterTransactionCompraHandler registerTransactionCompraHandler;

@SneakyThrows
@RabbitListener(queues = "${general.config.rabbitmq.queueCustomer}")
public void readLog(Notification message) throws JsonProcessingException {
    logger.info("Mensaje recibido: " + message);

    String item = message.getMessage();

    logger.info("Mensaje recibido: " + item);

    CustomerSaveDTO customerSaveDTO = JsonToObjectConverter.convertJsonToObject(item, CustomerSaveDTO.class);

    saveCustumerHandler.save(customerSaveDTO);

}


    @SneakyThrows
    @RabbitListener(queues = "${general.config.rabbitmq.queueTransactionDepositSucursal}")
    public void readRegisterTransactionDepositSucursal(Notification message) throws JsonProcessingException {
        logger.info("Mensaje recibido: " + message);

        String item = message.getMessage();

        logger.info("Mensaje recibido: " + item);

        BankTransactionDepositSucursal transaction = JsonToObjectConverter.convertJsonToObject(item, BankTransactionDepositSucursal.class);

        registerTransactionDepositSucursalHandler.apply(transaction);

    }

    @SneakyThrows
    @RabbitListener(queues = "${general.config.rabbitmq.queueTransactionDepositCajero}")
    public void readRegisterTransactionDepositCajero(Notification message) throws JsonProcessingException {
        logger.info("Mensaje recibido: " + message);

        String item = message.getMessage();

        logger.info("Mensaje recibido: " + item);

        BankTransactionDepositCajero transaction = JsonToObjectConverter.convertJsonToObject(item, BankTransactionDepositCajero.class);

        registerTransactionDepositCajeroHandler.apply(transaction);

    }

    @SneakyThrows
    @RabbitListener(queues = "${general.config.rabbitmq.queueTransactionDepositTransferencia}")
    public void readRegisterTransactionDepositTransferencia(Notification message) throws JsonProcessingException {
        logger.info("Mensaje recibido: " + message);

        String item = message.getMessage();

        logger.info("Mensaje recibido: " + item);

        BankTransactionDepositTransfer transaction = JsonToObjectConverter.convertJsonToObject(item, BankTransactionDepositTransfer.class);

        registerTransactionDepositTransferHandler.apply(transaction);

    }



    @SneakyThrows
    @RabbitListener(queues = "${general.config.rabbitmq.queueTransactionRetiroCajero}")
    public void readRegisterTransactionRetiroCajero(Notification message) throws JsonProcessingException {
        logger.info("Mensaje recibido: " + message);

        String item = message.getMessage();

        logger.info("Mensaje recibido: " + item);

        BankTransactionWithdrawFromATM transaction = JsonToObjectConverter.convertJsonToObject(item, BankTransactionWithdrawFromATM.class);

        registerTransactionWithDrawFromATMHandler.apply(transaction);

    }

    @SneakyThrows
    @RabbitListener(queues = "${general.config.rabbitmq.queueTransactionCompra}")
    public void readRegisterTransactionCompra(Notification message) throws JsonProcessingException {
        logger.info("Mensaje recibido: " + message);

        String item = message.getMessage();

        logger.info("Mensaje recibido: " + item);

        BankTransactionBuys transaction = JsonToObjectConverter.convertJsonToObject(item, BankTransactionBuys.class);

        registerTransactionCompraHandler.apply(transaction);

    }


}
