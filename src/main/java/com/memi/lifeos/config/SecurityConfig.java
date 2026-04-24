package com.memi.lifeos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	ApiKeyFilter apiKeyFilter(@Value("${app.api.key:}") String apiKey) {
		return new ApiKeyFilter(apiKey);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource(
		@Value("${app.cors.extra-patterns:}") String extraPatterns) {
		List<String> patterns = new ArrayList<>();
		patterns.addAll(List.of(
			"http://localhost:*",
			"https://localhost:*",
			"http://127.0.0.1:*",
			"https://127.0.0.1:*",
			"https://*.vercel.app",
			"https://*.ngrok-free.app",
			"https://*.ngrok.io"
		));
		if (StringUtils.hasText(extraPatterns)) {
			for (String p : extraPatterns.split(",")) {
				String t = p.trim();
				if (!t.isEmpty()) {
					patterns.add(t);
				}
			}
		}
		CorsConfiguration c = new CorsConfiguration();
		c.setAllowedOriginPatterns(patterns);
		c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));
		c.setAllowedHeaders(List.of("*"));
		c.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", c);
		return source;
	}

	@Bean
	SecurityFilterChain securityFilterChain(
		HttpSecurity http,
		ApiKeyFilter apiKeyFilter,
		CorsConfigurationSource corsConfigurationSource) throws Exception {
		return http
			.csrf(cs -> cs.disable())
			.cors(c -> c.configurationSource(corsConfigurationSource))
			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(apiKeyFilter, AnonymousAuthenticationFilter.class)
			.authorizeHttpRequests(a -> a
				.anyRequest()
				.permitAll()
			)
			.build();
	}

}
