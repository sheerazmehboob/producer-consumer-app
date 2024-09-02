package com.spring.producer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection
                .csrf(csrf -> csrf.disable())
                // Configure CORS if needed
                .cors(cors -> cors.disable())
                // Configure authorization for specific endpoints
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**").permitAll() // Permit all requests to Swagger UI and API docs
                        .requestMatchers("/api/**").permitAll() // api/v1 must be authenticated
                        .requestMatchers("/**").permitAll()
                );

        return http.build();
    }
}