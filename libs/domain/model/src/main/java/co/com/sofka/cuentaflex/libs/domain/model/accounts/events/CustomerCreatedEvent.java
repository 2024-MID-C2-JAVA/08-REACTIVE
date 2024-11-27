package co.com.sofka.cuentaflex.libs.domain.model.accounts.events;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.Customer;

public final class CustomerCreatedEvent extends DomainEvent {
    private String firstName;
    private String lastName;
    private String identification;

    public CustomerCreatedEvent() {
        super(
                CustomerCreatedEvent.class.getName(),
                Customer.class.getName()
        );
    }

    public CustomerCreatedEvent(String customerId, String firstName, String lastName, String identification) {
        super(
                CustomerCreatedEvent.class.getName(),
                customerId,
                Customer.class.getName()
        );

        this.firstName = firstName;
        this.lastName = lastName;
        this.identification = identification;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }
}
