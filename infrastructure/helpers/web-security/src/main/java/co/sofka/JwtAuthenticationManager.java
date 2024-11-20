package co.sofka;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtService jwtService;

    public JwtAuthenticationManager(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        if (jwtService.isTokenValid(token)) {
            String username = jwtService.extractUsername(token);

            UserDetails userDetails = User.withUsername(username)
                    .authorities(AuthorityUtils.NO_AUTHORITIES)
                    .password("")
                    .build();

            return Mono.just(new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities()));
        }

        return Mono.empty();
    }
}