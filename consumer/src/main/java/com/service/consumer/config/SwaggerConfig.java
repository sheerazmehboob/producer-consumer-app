package com.service.consumer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Year;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI config() {
        return new OpenAPI()
                .info(new Info().title("Consumer API")
                        .description("This is a consumer service that fetches data from Producer application")
                        .version(this.getClass().getPackage().getImplementationVersion())
                        .license(new License().name("(C) Copyright " + Year.now()).url("")));
    }
}
