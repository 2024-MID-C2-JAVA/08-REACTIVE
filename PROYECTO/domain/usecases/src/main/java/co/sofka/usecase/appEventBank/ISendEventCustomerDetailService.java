package co.sofka.usecase.appEventBank;


import co.sofka.Event;
import co.sofka.generic.DomainEvent;

@FunctionalInterface
public interface ISendEventCustomerDetailService {
    void save(DomainEvent item);


}
