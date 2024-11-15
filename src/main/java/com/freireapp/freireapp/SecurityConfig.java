package com.freireapp.freireapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Desabilita CSRF para propósitos de teste
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permite todas as requisições sem autenticação
                );
        return http.build();
    }
}