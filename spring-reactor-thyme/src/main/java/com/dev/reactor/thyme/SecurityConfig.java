package com.dev.reactor.thyme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityWebFilterChain webFilterChain(ServerHttpSecurity http) {
		return http.cors()
				.and()
				.authorizeExchange()
				.pathMatchers("/", "/css/**", "/js/**", "/img/**").permitAll()
				.pathMatchers("/login").permitAll()
				.pathMatchers("/logout").permitAll()
				.pathMatchers("/platos/**").hasAuthority("ADMIN")
				.pathMatchers("/facturas/**")
				.access((mono, context) -> mono
						.map(auth -> auth.getAuthorities()
								.stream()
								.filter(a -> a.getAuthority().equals("ADMIN"))
								.count() > 0)
						.map(AuthorizationDecision::new))
				.pathMatchers("/clientes/**")
				.access((mono, context) -> mono
						.map(auth -> auth.getAuthorities()
								.stream()
								.filter(a -> a.getAuthority().equals("ADMIN") || a.getAuthority().equals("USER"))
								.count() > 0)
						.map(AuthorizationDecision::new))
				.anyExchange().authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.and()
				.logout().logoutUrl("/logout")
				.and()
				//.csrf().disable()
				.csrf(csrf -> csrf.tokenFromMultipartDataEnabled(true))
				.exceptionHandling()
				.accessDeniedHandler(new HttpStatusServerAccessDeniedHandler(HttpStatus.FORBIDDEN))
				.and()
				.build();
				
	}
}