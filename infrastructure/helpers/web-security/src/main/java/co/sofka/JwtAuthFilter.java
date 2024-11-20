package co.sofka;

import com.mongodb.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements WebFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange,@NonNull WebFilterChain chain) {
        final String authHeader =
                exchange
                        .getRequest()
                        .getHeaders()
                        .getFirst("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return chain.filter(exchange);
        }

        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);

        if(userEmail != null){
            var authoritiesClaims = jwtService.extractAllClaims(jwt).get("roles");
            var authorities =
                    authoritiesClaims != null ?
                            AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaims.toString()) :
                            AuthorityUtils.NO_AUTHORITIES;
            UserDetails userDetails =
                    User
                            .withUsername(userEmail)
                            .password("")
                            .authorities(authorities)
                            .build();

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                authorities);
                return chain
                        .filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authToken));
            }
        }

        return chain.filter(exchange);
    }
}

