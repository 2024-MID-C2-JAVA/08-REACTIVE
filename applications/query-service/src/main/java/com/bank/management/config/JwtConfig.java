package com.bank.management.config;

import com.bank.management.filters.JwtAuthorizationFilter;
import com.bank.management.gateway.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class JwtConfig {


    @Bean
    public SecurityWebFilterChain securityFilterChain(
            ServerHttpSecurity http,
            JwtAuthorizationFilter jwtAuthFilter,
            ReactiveAuthenticationManager authManager) {


        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges ->
                        exchanges
                                .anyExchange()
                                .permitAll())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authenticationManager(authManager)
                .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(UserRepository repository) {
        return username -> repository.findByUsername(username)
                .flatMap(userRequest -> {
                    if (userRequest == null) {
                        return Mono.error(new UsernameNotFoundException("User not found"));
                    }
                    List<SimpleGrantedAuthority> authorities = userRequest.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    User userFound = new User(
                            userRequest.getUsername(),
                            userRequest.getPassword(),
                            authorities
                    );

                    return Mono.just(userFound);
                });
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(
            ReactiveUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        UserDetailsRepositoryReactiveAuthenticationManager authManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);

        authManager.setPasswordEncoder(passwordEncoder);
        return authManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
