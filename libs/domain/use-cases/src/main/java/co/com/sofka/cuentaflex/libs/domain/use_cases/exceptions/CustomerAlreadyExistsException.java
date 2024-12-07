package co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions;

public final class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String customerId) {
        super("The customer with identification '" + customerId + "' already exists.");
    }
}
