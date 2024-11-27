package co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions;

public class CustomerDoesNotExistsException extends RuntimeException {
    public CustomerDoesNotExistsException(String customerId) {
        super("The customer with identification " + customerId + " does not exists");
    }
}
