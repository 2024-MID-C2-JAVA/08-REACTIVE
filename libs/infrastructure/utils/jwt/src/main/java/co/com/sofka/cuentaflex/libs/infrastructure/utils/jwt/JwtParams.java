package co.com.sofka.cuentaflex.libs.infrastructure.utils.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class JwtParams {
    private final String secretKey;
    private final int expirationInHours;

    public JwtParams(
            @Value("${cuentaflex.jwt.secret-key}") String secretKey,
            @Value("${cuentaflex.jwt.expires-in-hours}") int expirationInHours
    ) {
        this.secretKey = secretKey;
        this.expirationInHours = expirationInHours;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public int getExpirationInHours() {
        return expirationInHours;
    }
}
