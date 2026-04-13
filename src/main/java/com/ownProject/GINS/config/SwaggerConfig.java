package com.ownProject.GINS.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customApiConfig() {
		
		return new OpenAPI()
				.info(
				new Info().title("Stock Pulse APIs")
						  .description("Enterprise-grade Inventory & Warehouse Management System developed by Mahek.")
						  .version("1.0.0")
						  .contact(new Contact().name("Mahek").email("mahekptl09@gmail.com"))
						  .license(new License().name("Apache 2.0").url("http://springdoc.org"))
				)
//				.servers(List.of(new Server().url("http://localhost:8888").description("local"),
//						new Server().url("http://localhost:8080").description("live")))
				.tags(List.of(
						new  Tag().name("Jwt Auth APIs"),
						new Tag().name("Product APIs"),
						new Tag().name("Ware_House APIs"),
						new Tag().name("Inventory APIs"),
						new Tag().name("Notification APIs"),
						new Tag().name("Transaction APIs")
						)
					 )
				.addSecurityItem(new SecurityRequirement().addList("bearerAuth")) // Global security requirement
	            .components(new Components()
	                .addSecuritySchemes("bearerAuth", 
	                    new SecurityScheme()
	                        .name("Authorization")
	                        .type(SecurityScheme.Type.HTTP)
	                        .scheme("bearer")
	                        .bearerFormat("JWT")
	            			.in(SecurityScheme.In.HEADER)
	            	)
	            )
	            ;
				
	}

	
}
