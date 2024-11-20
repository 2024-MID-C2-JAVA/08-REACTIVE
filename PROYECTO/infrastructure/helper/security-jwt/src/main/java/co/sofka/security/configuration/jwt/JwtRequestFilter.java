package co.sofka.security.configuration.jwt;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.io.IOException;

@Component
public class JwtRequestFilter implements WebFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private String generalPass = "@@##g3n3r4lP4SS$$";

	@Value("${general.config.constans.BEARER}")
	public String BEARER;

	@Value("${general.config.constans.AUTHORIZATION}")
	public String AUTHORIZATION = "Authorization";

	@Value("${general.config.constans.authorities}")
	public String AUTHORITIES ;

	@Value("${general.config.constans.id}")
	public String ID = "bancoAPIJWT";

	@Value("${URL.OpenEndpointsRegex}")
	public String URLFree ;

	@Override
	public Mono<Void> filter( @NonNull ServerWebExchange exchange,
										   @NonNull WebFilterChain filterChain
	)
			 {

				 String username = null;
				 String jwtToken = null;

				 final String authHeader =
						 exchange
								 .getRequest()
								 .getHeaders()
								 .getFirst("Authorization");

				 if (authHeader == null || !authHeader.startsWith("Bearer ")) {
					 return filterChain.filter(exchange);
				 }

				 jwtToken = authHeader.substring(7);
				 username =  jwtTokenUtil.getUsernameFromToken(jwtToken);;


				 if(username != null) {


					 // lógica a ejecutar si existe un usuario en el JWT
					 UserDetails userDetails = new User(username,generalPass, jwtTokenUtil.getAuthoritiesUsernameFromToken(jwtToken));

					 logger.info("Datos del usuario {}, roles {}",userDetails.getUsername(),userDetails.getAuthorities());


				 }

				 UserDetails userDetails = new User(username,generalPass, jwtTokenUtil.getAuthoritiesUsernameFromToken(jwtToken));
				 if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {


					 UsernamePasswordAuthenticationToken authToken =
							 new UsernamePasswordAuthenticationToken(
									 userDetails,
									 null,
									 userDetails.getAuthorities());
					 return filterChain
							 .filter(exchange)
							 .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authToken));
				 }


				 return filterChain.filter(exchange);

//				 final String requestTokenHeader = request.getHeader(AUTHORIZATION);
//		String username = null;
//		String jwtToken = null;
//		String requestURI = request.getRequestURI();
//		System.out.println(requestURI);
//
//		if (!URLFree.contains(requestURI)){
//			if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER)) {
//				jwtToken = requestTokenHeader.substring(7);
//				logger.info("Token resivido es  {}",jwtToken);
//				try {
//					username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//				} catch (IllegalArgumentException e) {
//				} catch (Exception e) {
//				}
//			} else {
//				logger.warn("JWT Token does not begin with Bearer String");
//			}
//			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//				UserDetails userDetails = new User(username,generalPass, jwtTokenUtil.getAuthoritiesUsernameFromToken(jwtToken));
//
//				logger.info("Datos del usuario {}, roles {}",userDetails.getUsername(),userDetails.getAuthorities());
//
//				if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
//					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//							userDetails, null, userDetails.getAuthorities());
//					usernamePasswordAuthenticationToken
//							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//				}
//			}
//		}
//
//
//		chain.doFilter(request, response);
	}

}