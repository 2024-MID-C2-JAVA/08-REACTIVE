package co.com.sofka.cuentaflex.libs.domain.ports.utils.encryption;

public class FailedDecryptionException extends RuntimeException {
    public FailedDecryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
