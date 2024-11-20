package co.sofka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, ReactiveAuthenticationManager reactiveAuthenticationManager) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges ->
                        exchanges
                                .anyExchange().permitAll()
                )
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authenticationManager(reactiveAuthenticationManager)
                .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

}

