package com.example.learn_app.learn_app.configuration;

import com.example.learn_app.learn_app.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests((requests) -> requests

                                                .requestMatchers("/api/auth/register", "/api/auth/login")
                                                .permitAll()
                                                .anyRequest().authenticated()

                                )
                                .userDetailsService(customUserDetailsService)
                                .httpBasic(Customizer
                                                .withDefaults())
                                .formLogin((form) -> form.disable())
                                .logout((logout) -> logout.permitAll())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .csrf(csrf -> csrf.disable());
                return http.build();
        }

}
