package co.com.sofka.cuentaflex.libs.domain.model.accounts.commands;

import co.com.sofka.cuentaflex.libs.domain.model.Command;

public final class CreateCustomerCommand extends Command {
    private final String customerId;
    private final String firstName;
    private final String lastName;
    private final String identification;

    public CreateCustomerCommand(String customerId, String firstName, String lastName, String identification) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identification = identification;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getIdentification() {
        return this.identification;
    }
}
