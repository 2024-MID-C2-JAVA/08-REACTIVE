package com.bank.management.filters;

import com.bank.management.JwtUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNullApi;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(
            @NonNull ServerWebExchange exchange,
            @NonNull WebFilterChain filterChain) {

        final String authHeader =
                exchange
                        .getRequest()
                        .getHeaders()
                        .getFirst("Authorization");



        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return filterChain.filter(exchange);
        }


        String jwt = authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Object rolesClaim = jwtUtil.extractAllClaims(jwt).get("roles");

            List<GrantedAuthority> authorities;
            if (rolesClaim instanceof List<?>) {
                authorities = ((List<?>) rolesClaim).stream()
                        .filter(role -> role instanceof String)
                        .map(role -> new SimpleGrantedAuthority((String) role))
                        .collect(Collectors.toList());
            } else {
                authorities = AuthorityUtils.NO_AUTHORITIES;
            }
            UserDetails userDetails = User.withUsername(username)
                    .password("")
                    .authorities(authorities)
                    .build();

            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                authorities);
                return filterChain
                        .filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authToken));
            }
        }
        return filterChain.filter(exchange);
    }
}
