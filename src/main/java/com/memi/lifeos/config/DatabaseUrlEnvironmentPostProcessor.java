package com.memi.lifeos.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Map Railway / Heroku {@code postgres://} / {@code postgresql://} into Spring Boot JDBC
 * so the container works without a checked-in {@code local.properties}.
 * Skips if {@code spring.datasource.url} is already set.
 */
public class DatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		if (StringUtils.hasText(environment.getProperty("spring.datasource.url"))
			|| StringUtils.hasText(environment.getProperty("spring.datasource.jdbc-url"))) {
			return;
		}
		String databaseUrl = environment.getProperty("DATABASE_URL");
		if (databaseUrl == null || !databaseUrl.startsWith("postgres")) {
			return;
		}
		JdbcParts parts;
		try {
			parts = parsePostgresUrl(databaseUrl);
		} catch (RuntimeException e) {
			throw new IllegalStateException("Invalid DATABASE_URL; cannot start DataSource. " + e.getMessage(), e);
		}
		Map<String, Object> m = new HashMap<>();
		m.put("spring.datasource.url", parts.jdbcUrl);
		if (StringUtils.hasText(parts.username)) {
			m.put("spring.datasource.username", parts.username);
		}
		if (StringUtils.hasText(parts.password)) {
			m.put("spring.datasource.password", parts.password);
		}
		environment.getPropertySources().addFirst(
			new MapPropertySource("railwayDatabaseUrl", m)
		);
	}

	private static JdbcParts parsePostgresUrl(String databaseUrl) {
		URI u = URI.create(forgeUriHttpLike(databaseUrl));
		String userInfo = u.getUserInfo();
		String user = "postgres";
		String password = "";
		if (StringUtils.hasText(userInfo)) {
			int colon = userInfo.indexOf(':');
			if (colon >= 0) {
				user = decode(userInfo.substring(0, colon));
				password = decode(userInfo.substring(colon + 1));
			} else {
				user = decode(userInfo);
			}
		}
		int port = u.getPort() > 0 ? u.getPort() : 5432;
		String path = u.getPath();
		if (path == null) {
			path = "";
		}
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		String host = u.getHost();
		if (host == null) {
			throw new IllegalStateException("missing host");
		}
		String base = "jdbc:postgresql://" + host + ":" + port + "/"
			+ (path.isEmpty() ? "postgres" : path);
		String q = u.getQuery();
		String jdbcUrl;
		if (StringUtils.hasText(q)) {
			// e.g. sslmode=require; avoid duplicating
			if (!q.contains("sslmode")) {
				jdbcUrl = base + "?" + q + "&sslmode=require";
			} else {
				jdbcUrl = base + "?" + q;
			}
		} else {
			jdbcUrl = base + "?sslmode=require";
		}
		return new JdbcParts(jdbcUrl, user, password);
	}

	private static String decode(String s) {
		if (s == null || s.isEmpty()) {
			return s;
		}
		try {
			return URLDecoder.decode(s, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@link URI#parse} wants a known scheme; swap so authority parses.
	 */
	private static String forgeUriHttpLike(String databaseUrl) {
		if (databaseUrl.startsWith("postgresql://")) {
			return "https://" + databaseUrl.substring("postgresql://".length());
		}
		if (databaseUrl.startsWith("postgres://")) {
			return "https://" + databaseUrl.substring("postgres://".length());
		}
		return databaseUrl;
	}

	private record JdbcParts(String jdbcUrl, String username, String password) {
	}
}
