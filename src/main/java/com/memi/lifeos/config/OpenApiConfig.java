package com.memi.lifeos.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI openAPI() {
		return new OpenAPI()
			.info(
				new Info()
					.title("Life OS Backend")
					.description("REST API. If `API_KEY` (app.api.key) is set, send header `X-API-Key` for "
						+ "write operations to `/api`.")
					.version("0.0.1")
			)
			.externalDocs(
				new ExternalDocumentation()
					.description("Repository")
					.url("https://github.com/catBible/memi-backend")
			)
			.components(
				new Components()
					.addSecuritySchemes("X-API-Key", new SecurityScheme()
						.type(SecurityScheme.Type.APIKEY)
						.in(SecurityScheme.In.HEADER)
						.name("X-API-Key")
						.description("Same value as server `API_KEY` (when enabled).")
					)
			);
	}
}
