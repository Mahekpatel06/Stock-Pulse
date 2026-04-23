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
				.tags(List.of(
					new Tag().name("Jwt Auth APIs")
                        .description("Endpoints for user authentication and JWT token generation"),
                    new Tag().name("Product APIs")
                        .description("Manage the product catalog, including adding, updating, and retrieving product details."),
                    new Tag().name("Ware_House APIs")
                        .description("Manage physical warehouse locations and capacities"),
                    new Tag().name("Inventory APIs")
                        .description("Track real-time stock levels, stock-ins, and stock-outs across different warehouses."),
                    new Tag().name("Notification APIs")
                        .description("Manage system alerts, low-stock warnings, and user communication logs."),
                    new Tag().name("Transaction APIs")
                        .description("Audit trail for all financial and stock movements within the Stock Pulse system.")
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
