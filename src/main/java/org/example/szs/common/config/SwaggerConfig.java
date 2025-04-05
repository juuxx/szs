package org.example.szs.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@OpenAPIDefinition(
	tags = {
		@Tag(name = "01-인증", description = "로그인 / 회원가입"),
		@Tag(name = "02-스크래핑", description = "스크래핑 데이터 조회")
		// ,
		// @Tag(name = "03-산출세액", description = "산출세액 계산")
	}
)
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.addSecurityItem(new SecurityRequirement().addList("bearer-key"))
			.components(new Components()
				.addSecuritySchemes("bearer-key", new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")
					.in(SecurityScheme.In.HEADER)
					.name("Authorization")
				))
			.info(apiInfo())
			.addSecurityItem(new SecurityRequirement().addList("bearer-key"));
	}

	private Info apiInfo() {
		return new Info()
			.title("🧾 삼쩜삼 백엔드 과제 API")
			.description("회원가입 및 스크래핑 조회, 계산 서비스")
			.version("v1.0.0")
			.contact(new Contact()
				.name("서명주")
				.email("juuxmee@gmail.com"));
	}
}
