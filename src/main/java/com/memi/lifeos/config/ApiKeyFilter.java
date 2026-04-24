package com.memi.lifeos.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * If {@code app.api.key} (env {@code API_KEY}) is non-blank, requires matching {@code X-API-Key}
 * for mutating methods on {@code /api} paths. GET, HEAD, OPTIONS, etc. are always allowed.
 */
public class ApiKeyFilter extends OncePerRequestFilter {

	private final String expectedKey;

	public ApiKeyFilter(String expectedKey) {
		this.expectedKey = expectedKey;
	}

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		if (!StringUtils.hasText(expectedKey)) {
			filterChain.doFilter(request, response);
			return;
		}
		String m = request.getMethod();
		if (HttpMethod.GET.matches(m) || HttpMethod.HEAD.matches(m) || HttpMethod.OPTIONS.matches(m)) {
			filterChain.doFilter(request, response);
			return;
		}
		String sp = request.getServletPath() != null ? request.getServletPath() : "";
		if (!sp.startsWith("/api")) {
			filterChain.doFilter(request, response);
			return;
		}
		String key = request.getHeader("X-API-Key");
		if (expectedKey.equals(key)) {
			filterChain.doFilter(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing X-API-Key");
		}
	}
}
