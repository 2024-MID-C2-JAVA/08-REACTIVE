package co.com.sofka.cuentaflex.libs.domain.ports.utils.encryption;

public class FailedEncryptionException extends RuntimeException {
    public FailedEncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
