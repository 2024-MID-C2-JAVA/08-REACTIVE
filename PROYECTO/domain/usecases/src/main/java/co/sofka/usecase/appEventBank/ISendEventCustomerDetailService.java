package co.sofka.usecase.appEventBank;


import co.sofka.Event;

@FunctionalInterface
public interface ISendEventCustomerDetailService {
    void save(Event item);


}
