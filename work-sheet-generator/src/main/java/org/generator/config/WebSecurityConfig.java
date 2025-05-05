package org.generator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Configuration class for Spring Security settings.
 * Enables web security features for the application.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    /**
     * Configures the security filter chain for HTTP requests.
     * Defines authorization rules, disables CSRF protection, and enables CORS.
     * @param {HttpSecurity} http The {@link HttpSecurity} object to configure.
     * @return {SecurityFilterChain} The built security filter chain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}); // activează CORS folosind bean-ul CorsConfigurationSource

        return http.build();
    }
}