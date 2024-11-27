package co.sofka.security.configuration.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenProvider {
    String generateToken(UserDetails userDetails);
}
