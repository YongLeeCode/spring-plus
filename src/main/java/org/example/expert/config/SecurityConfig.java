package org.example.expert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/auth/**").permitAll()
				.requestMatchers("/users/search").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/users").hasRole("ADMIN")
				.anyRequest().authenticated()
			)
			.formLogin(formLogin -> formLogin.disable())
			.httpBasic(httpBasic -> httpBasic.disable())
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.exceptionHandling(exceptions -> exceptions
				.authenticationEntryPoint((request, response, authException) -> {
					response.sendError(401, "Unauthorized: " + authException.getMessage());
				})
				.accessDeniedHandler((request, response, accessDeniedException) -> {
					response.sendError(403, "Access Denied: " + accessDeniedException.getMessage());
				})
			)
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
