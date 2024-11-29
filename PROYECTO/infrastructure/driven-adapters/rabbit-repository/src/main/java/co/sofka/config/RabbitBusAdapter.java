package co.sofka.config;


import co.sofka.event.Notification;
import co.sofka.event.TransactionAdd;
import co.sofka.gateway.IRabbitBus;
import co.sofka.generic.DomainEvent;
import co.sofka.serializer.JSONMapper;
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

    @Value("${general.config.rabbitmq.queue}")
    private String queueAccount;

    @Value("${general.config.rabbitmq.routingKey}")
    private String routingAccountkey;



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

    private final TokenByDinHeaders tokenByDinHeaders;

    private final JSONMapper eventSerializer;

    private static final Logger logger = LoggerFactory.getLogger(RabbitBusAdapter.class);

    public RabbitBusAdapter(JSONMapper eventSerializer,TokenByDinHeaders tokenByDinHeaders,RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.tokenByDinHeaders = tokenByDinHeaders;
        this.eventSerializer = eventSerializer;
    }

    @Override
    public void send(DomainEvent event) {

        logger.info("Sending notification to RabbitMQ: {}",event);

        Notification notification = new Notification();
        notification.setMessage(wrapEvent(event,eventSerializer));
        //notification.setMessage(tokenByDinHeaders.encode(event.getBody()));
        notification.setWhen(Instant.now());
        notification.setType(event.type);
        notification.setUuid(event.aggregateRootId());


        switch (notification.getType()) {
            case "CreateCustomer":
                rabbitTemplate.convertAndSend(exchange, routingkey, notification);
                break;
            case "CreateAccount":
                rabbitTemplate.convertAndSend(exchange, routingAccountkey, notification);
                break;
            case "CreateTransaction":
                TransactionAdd event1 = (TransactionAdd) event;
                logger.info("Sending notification to RabbitMQ: {} {}",notification.getType(), event1.getTypeTransaction());
                if (event1.getTypeTransaction().equals("TransactionDepositSucursal")) {
                    rabbitTemplate.convertAndSend(exchange, routingTransactionDepositSucursalKey, notification);
                } else if (event1.getTypeTransaction().equals("TransactionDepositCajero")) {
                    rabbitTemplate.convertAndSend(exchange, routingTransactionDepositCajeroKey, notification);
                } else if (event1.getTypeTransaction().equals("TransactionDepositTransferencia")) {
                    rabbitTemplate.convertAndSend(exchange, routingTransactionDepositTransferenciaKey, notification);
                } else if (event1.getTypeTransaction().equals("TransactionRetiroCajero")) {
                    rabbitTemplate.convertAndSend(exchange, routingTransactionRetiroCajeroKey, notification);
                } else if (event1.getTypeTransaction().equals("TransactionCompra")) {
                    rabbitTemplate.convertAndSend(exchange, routingTransactionCompraKey, notification);
                }
                break;
            default:
                logger.info("No se ha encontrado el tipo de evento");
                break;
        }

    }


    public static String wrapEvent(DomainEvent domainEvent, JSONMapper eventSerializer){
        return eventSerializer.writeToJson(domainEvent);
    }
}
