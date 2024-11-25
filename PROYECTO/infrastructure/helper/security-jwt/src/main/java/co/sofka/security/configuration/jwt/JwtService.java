package co.sofka.security.configuration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService implements TokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expirationDateInMs}")
    private long tokenExpiration;

    String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    List<String> extractRoles(String jwt) {
        return extractClaim(jwt, claims -> (List<String>) claims.get("roles"));
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateTokenJWT(Map.of(), userDetails);
    }

    boolean isTokenValid(String jwt) {
        return !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        return extractClaim(jwt, Claims::getExpiration).before(new Date());
    }

    public String generateTokenJWT(Map<String, Object> extraClaims, UserDetails userDetails) {
        long currentTimeMillis = System.currentTimeMillis();

        return Jwts.builder()
//                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(role -> role.substring("ROLE_".length()))
                        .toArray())
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + tokenExpiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    private <T> T extractClaim(String jwt, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(jwt);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwt) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();

        } catch (JwtException e) {
            throw new JwtAuthenticationException(e.getMessage());
        }
    }

    private SecretKey getSigningKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String encryptionPassword(String passwordActual) {

        PasswordEncoder bCryptPasswordEncoder;
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(passwordActual);
    }


    public Boolean matchesPasswd(String passwordActual, String passwordBD) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(passwordActual, passwordBD);
    }
}
