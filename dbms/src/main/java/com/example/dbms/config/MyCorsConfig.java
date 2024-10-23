package com.example.dbms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class MyCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:5173"); // Allow the client origin
        corsConfiguration.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, etc.)
        corsConfiguration.addAllowedHeader("*"); // Allow all headers
        corsConfiguration.setAllowCredentials(true); // Allow cookies or credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Apply to all endpoints

        return new CorsFilter(source);
    }
}