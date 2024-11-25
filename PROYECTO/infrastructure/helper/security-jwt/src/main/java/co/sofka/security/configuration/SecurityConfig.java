package co.sofka.security.configuration;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
@AllArgsConstructor
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                     ReactiveAuthenticationManager authenticationManager,
                                                     ServerAuthenticationConverter authenticationConverter) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter);

        return http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Arrays.asList("*"));
                    configuration.setAllowedMethods(Arrays.asList("*"));
                    configuration.setAllowedHeaders(Arrays.asList("*"));
                    return configuration;
                }))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/utils/generate","/utils/login").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .cors(ServerHttpSecurity.CorsSpec::disable)
                .build();
    }

//    private JwtRequestFilter jwtRequestFilter;
//
//
//    @Bean
//    public SecurityWebFilterChain securityFilterChain(
//            ServerHttpSecurity http,
//            JwtRequestFilter jwtAuthFilter,
//            ReactiveAuthenticationManager authManager) {
//
//
//        return http
//                .cors(cors -> cors.configurationSource(request -> {
//                    CorsConfiguration configuration = new CorsConfiguration();
//                    configuration.setAllowedOrigins(Arrays.asList("*"));
//                    configuration.setAllowedMethods(Arrays.asList("*"));
//                    configuration.setAllowedHeaders(Arrays.asList("*"));
//                    return configuration;
//                }))
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(exchanges ->
//                        exchanges
//                                .pathMatchers("/utils/generate")
//                                .permitAll()
//                                .pathMatchers("/v3/api-docs/**","/v3/api-docs.yaml",
//                                        "/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html")
//                                .permitAll()
//                                .anyExchange()
//                                .authenticated())
//                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
//                .authenticationManager(authManager)
//                .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
//                .build();
//    }



//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
//    {
//
//        logger.info("Configuracion inicial");
//
//        return http
//                .cors(cors -> cors.configurationSource(request -> {
//                    CorsConfiguration configuration = new CorsConfiguration();
//                    configuration.setAllowedOrigins(Arrays.asList("*"));
//                    configuration.setAllowedMethods(Arrays.asList("*"));
//                    configuration.setAllowedHeaders(Arrays.asList("*"));
//                    return configuration;
//                }))
//                .csrf(csrf ->
//                        csrf
//                                .disable())
//                .authorizeHttpRequests(authRequest ->
//                        authRequest
//                                .requestMatchers("/v3/api-docs/**","/v3/api-docs.yaml",
//                                        "/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html")
//                                .permitAll()
//                                .requestMatchers
//                                        ("/v3/api-docs/**",
//                                                "/configuration/ui",
//                                                "/swagger-resources/**",
//                                                "/swagger-ui.html",
//                                                "/webjars/**").permitAll()
//                                .requestMatchers("/utils/generate").permitAll()
//                                .requestMatchers("/log/**").hasAuthority("ADMIN")
//                                .anyRequest().authenticated()
//                )
//                .sessionManagement(sessionManager->
//                        sessionManager
//                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .exceptionHandling(e-> e.accessDeniedHandler(jwtCustomAccessDenaiedHandler)
//                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
//                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//
//
//    }

}