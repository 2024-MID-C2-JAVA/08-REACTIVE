package co.sofka.config;


import co.sofka.Event;
import co.sofka.LogEvent;
import co.sofka.event.Notification;
import co.sofka.gateway.IRabbitBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RabbitBusAdapter implements IRabbitBus {

    @Value("${general.config.rabbitmq.exchange}")
    private String exchange;

    @Value("${general.config.rabbitmq.queueCustomer}")
    private String queue;

    @Value("${general.config.rabbitmq.routingCustomerKey}")
    private String routingkey;



    /* Transaction Deposito Sucursal*/

    @Value("${general.config.rabbitmq.queueTransactionDepositSucursal}")
    private String queueTransactionDepositSucursal;

    @Value("${general.config.rabbitmq.routingTransactionDepositSucursalKey}")
    private String routingTransactionDepositSucursalKey;

    /* Transaction Deposito Cajeto*/
    @Value("${general.config.rabbitmq.queueTransactionDepositCajero}")
    private String queueTransactionDepositCajero;

    @Value("${general.config.rabbitmq.routingTransactionDepositCajeroKey}")
    private String routingTransactionDepositCajeroKey;

    /* Transaction Deposito Transferencia*/

    @Value("${general.config.rabbitmq.queueTransactionDepositTransferencia}")
    private String queueTransactionDepositTransferencia;

    @Value("${general.config.rabbitmq.routingTransactionDepositTransferenciaKey}")
    private String routingTransactionDepositTransferenciaKey;


    /* Transaction Retiro ATM*/

    @Value("${general.config.rabbitmq.queueTransactionRetiroCajero}")
    private String queueTransactionRetiroCajero;

    @Value("${general.config.rabbitmq.routingTransactionRetiroCajeroKey}")
    private String routingTransactionRetiroCajeroKey;


    /* Transaction Compra*/

    @Value("${general.config.rabbitmq.queueTransactionCompra}")
    private String queueTransactionCompra;

    @Value("${general.config.rabbitmq.routingTransactionCompraKey}")
    private String routingTransactionCompraKey;

    private final RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RabbitBusAdapter.class);

    public RabbitBusAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void send(Event event) {

        Notification notification = new Notification();
        notification.setMessage(event.getBody());
        notification.setWhen(Instant.now());
        notification.setType(event.getType());
        notification.setUuid(event.getParentId());

        logger.info("Sending notification to RabbitMQ: {} {}",notification.getType(), notification);
        switch (notification.getType()) {
            case "Customer Created":
                rabbitTemplate.convertAndSend(exchange, routingkey, notification);
                break;
            case "TransactionDepositSucursal":
                rabbitTemplate.convertAndSend(exchange, routingTransactionDepositSucursalKey, notification);
                break;
            case "TransactionDepositCajero":
                rabbitTemplate.convertAndSend(exchange, routingTransactionDepositCajeroKey, notification);
                break;
            case "TransactionDepositTransferencia":
                rabbitTemplate.convertAndSend(exchange, routingTransactionDepositTransferenciaKey, notification);
                break;
            case "TransactionRetiroCajero":
                rabbitTemplate.convertAndSend(exchange, routingTransactionRetiroCajeroKey, notification);
                break;
            case "TransactionCompra":
                rabbitTemplate.convertAndSend(exchange, routingTransactionCompraKey, notification);
                break;
            default:
                logger.info("No se ha encontrado el tipo de evento");
                break;
        }

    }
}
