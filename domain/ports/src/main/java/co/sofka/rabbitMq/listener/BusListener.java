package co.sofka.rabbitMq.listener;


public interface BusListener {
    void receiveAccountEvent(String json);
    void receiveTransactionEvent(String json);
    void receiveUserEvent(String json);
}
