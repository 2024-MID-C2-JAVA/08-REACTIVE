package co.sofka.config;


import co.sofka.LogEvent;
import co.sofka.event.Notification;
import co.sofka.gateway.IRabbitBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitBusAdapter implements IRabbitBus {

    @Value("${general.config.rabbitmq.exchange}")
    private String exchange;

    @Value("${general.config.rabbitmq.queueCustomer}")
    private String queue;

    @Value("${general.config.rabbitmq.routingCustomerKey}")
    private String routingkey;

    /* Transaction */

    @Value("${general.config.rabbitmq.queueTransactionDepositSucursal}")
    private String queueTransactionDepositSucursal;

    @Value("${general.config.rabbitmq.routingTransactionDepositSucursalKey}")
    private String routingTransactionDepositSucursalKey;

    private final RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RabbitBusAdapter.class);

    public RabbitBusAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void send(Notification notification) {
        logger.info("Sending notification to RabbitMQ: {}", notification);
        switch (notification.getType()) {
            case "Customer Created":
                rabbitTemplate.convertAndSend(exchange, routingkey, notification);
                break;
            case "TransactionDepositSucursal":
                rabbitTemplate.convertAndSend(exchange, routingTransactionDepositSucursalKey, notification);
                break;
            default:
                routingkey = "general";
                break;
        }

    }
}
