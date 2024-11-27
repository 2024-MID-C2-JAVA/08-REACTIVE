package co.sofka.usecase.appEventBank;




import co.sofka.LogEvent;
import co.sofka.event.Notification;

@FunctionalInterface
public interface ISendEventCustomerDetailService {
    void save(Notification item);


}
