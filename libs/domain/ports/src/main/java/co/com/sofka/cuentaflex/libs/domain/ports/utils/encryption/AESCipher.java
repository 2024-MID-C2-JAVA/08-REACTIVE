package co.com.sofka.cuentaflex.libs.domain.ports.utils.encryption;

public interface AESCipher {
    String encrypt(String plainText, String symmetricKey, String initializationVector) throws FailedEncryptionException;
    String decrypt(String cipherText, String symmetricKey, String initializationVector) throws FailedDecryptionException;
}
