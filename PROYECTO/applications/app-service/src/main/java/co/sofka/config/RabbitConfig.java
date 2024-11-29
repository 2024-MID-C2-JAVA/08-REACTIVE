package co.sofka.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    @Value("${general.config.rabbitmq.exchange}")
    private String exchange;

    /* bank Generic */
    @Value("${general.config.rabbitmq.queue}")
    private String queue;

    @Value("${general.config.rabbitmq.routingkey}")
    private String routingkey;


    /* Customer */

    @Value("${general.config.rabbitmq.queueCustomer}")
    private String queueCustomer;

    @Value("${general.config.rabbitmq.routingCustomerKey}")
    private String routingCustomerKey;



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


    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchange);
    }


    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with(routingkey);
    }


    /* Transaction Deposito Sucursal*/
    @Bean
    public Queue queueTransactionDepositSucursal() {
        return new Queue(queueTransactionDepositSucursal);
    }

    @Bean
    public Binding bindingTransactionDepositSucursal(Queue queueTransactionDepositSucursal, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueTransactionDepositSucursal).to(topicExchange).with(routingTransactionDepositSucursalKey);
    }

/* Transaction Deposito Cajero*/
    @Bean
    public Queue queueTransactionDepositCajero() {
        return new Queue(queueTransactionDepositCajero);
    }

    @Bean
    public Binding bindingTransactionDepositCajero(Queue queueTransactionDepositCajero, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueTransactionDepositCajero).to(topicExchange).with(routingTransactionDepositCajeroKey);
    }


    /* Transaction Deposito Transferencia*/
    @Bean
    public Queue queueTransactionDepositTransferencia() {
        return new Queue(queueTransactionDepositTransferencia);
    }

    @Bean
    public Binding bindingTransactionDepositTransferencia(Queue queueTransactionDepositTransferencia, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueTransactionDepositTransferencia).to(topicExchange).with(routingTransactionDepositTransferenciaKey);
    }


    /* Transaction Retiro Cajero*/
    @Bean
    public Queue queueTransactionRetiroCajero() {
        return new Queue(queueTransactionRetiroCajero);
    }

    @Bean
    public Binding bindingTransactionRetiroCajero(Queue queueTransactionRetiroCajero, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueTransactionRetiroCajero).to(topicExchange).with(routingTransactionRetiroCajeroKey);
    }



    /* Transaction Compra*/
    @Bean
    public Queue queueTransactionCompra() {
        return new Queue(queueTransactionCompra);
    }

    @Bean
    public Binding bindingTransactionCompra(Queue queueTransactionCompra, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueTransactionCompra).to(topicExchange).with(routingTransactionCompraKey);
    }



    @Bean
    public Queue queueCustomer() {
        return new Queue(queueCustomer);
    }
    @Bean
    public Binding bindingCustomer(Queue queueCustomer, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueCustomer).to(topicExchange).with(routingCustomerKey);
    }



    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public AmqpTemplate rabbitTemplateBean(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }


}
