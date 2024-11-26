package co.com.sofka.cuentaflex.libs.infrastructure.utils.encryption;

import co.com.sofka.cuentaflex.libs.domain.ports.utils.encryption.AESCipher;
import co.com.sofka.cuentaflex.libs.domain.ports.utils.encryption.FailedDecryptionException;
import co.com.sofka.cuentaflex.libs.domain.ports.utils.encryption.FailedEncryptionException;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public final class Utf8KeyBase64EncodedAESCipher implements AESCipher {

    @Override
    public String encrypt(String plainText, String symmetricKey, String initializationVector) throws FailedEncryptionException {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(symmetricKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new FailedEncryptionException("An error occurred while encrypting", e);
        }
    }

    @Override
    public String decrypt(String cipherText, String symmetricKey, String initializationVector) throws FailedDecryptionException {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(symmetricKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encryptedData = Base64.getDecoder().decode(cipherText);
            return new String(cipher.doFinal(encryptedData), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new FailedDecryptionException("An error occurred while decrypting", e);
        }
    }
}
