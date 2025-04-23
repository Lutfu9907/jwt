package com.lutfudolay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI() 
				.info(new Info()
				.title("swagger geldi")
				.description("swagger öğreniyorum"))
				.addSecurityItem(new SecurityRequirement().addList("bearerAuth")) // Bu tanımlanan güvenlik şemasını Swagger belgelerine uygula demektir.
				.components(new Components().addSecuritySchemes("bearerAuth", // Bu satırda Swagger UI'da bir Authorize butonu gösteririz.
						new SecurityScheme()								// Bu butona tıklayıp token girince,tüm güvenli endpointlere otomatik olarak bu token'ı göndeririz.
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
						.description("JWT token değerini giriniz")));
	}
}