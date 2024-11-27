package co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions;

public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String customerId) {
        super("The customer with identification '" + customerId + "' already exists.");
    }
}
