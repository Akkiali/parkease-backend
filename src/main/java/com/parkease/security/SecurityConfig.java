package com.parkease.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

        .cors(cors -> {})
        .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
            		
            		.requestMatchers(
            		        HttpMethod.OPTIONS,
            		        "/**")
            		.permitAll()

                // PUBLIC APIs

                .requestMatchers(
                        "/api/auth/**",
                        "/api/locations/**")
                .permitAll()

                // DASHBOARD (ADMIN ONLY)

                .requestMatchers(
                        "/api/dashboard/**")
                .hasRole("ADMIN")

                // LOCATIONS

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/locations/**")
                .hasAnyRole("ADMIN", "USER")

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/locations/**")
                .hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/locations/**")
                .hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/locations/**")
                .hasRole("ADMIN")

                // SLOTS

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/slots/**")
                .hasAnyRole("ADMIN", "USER")

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/slots/**")
                .hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/slots/**")
                .hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/slots/**")
                .hasRole("ADMIN")

                // BOOKINGS

                .requestMatchers(
                        "/api/bookings/**")
                .hasAnyRole(
                        "ADMIN",
                        "USER")

                .anyRequest()
                .authenticated())

            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173"));

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"));

        configuration.setAllowedHeaders(
                List.of("*"));

        configuration.setAllowCredentials(
                true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration);

        return source;
    }
}