package com.example.developerteam.security;

import com.example.developerteam.security.JwtAuthenticationFilter;
import com.example.developerteam.security.JwtAuthorizationFilter;
import com.example.developerteam.security.JwtUtils;
import com.example.developerteam.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Отримуємо AuthenticationManager з AuthenticationConfiguration
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authManager) throws Exception {

        // Ініціалізуємо фільтри для JWT
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(authManager, jwtUtils);
        jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");

        JwtAuthorizationFilter jwtAuthorizationFilter =
                new JwtAuthorizationFilter(authManager, jwtUtils, userDetailsService);

        http
                // 1) вимикаємо CSRF (token-based)
                .csrf(csrf -> csrf.disable())

                // 2) робимо конфігурацію сесій stateless (JWT без сесій)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3) налаштовуємо правила авторизації HTTP-запитів
                .authorizeHttpRequests(auth -> auth
                        // дозволяємо всім робити POST /auth/register і POST /auth/login
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        // весь шлях /api/admin/** тільки для ROLE_ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // усі інші /api/** — лише аутентифіковані користувачі (ROLE_USER чи ROLE_ADMIN)
                        .requestMatchers("/api/**").authenticated()

                        // решта ресурсів — всі можуть звертатися
                        .anyRequest().permitAll()
                )

                // 4) вказуємо, що UserDetailsService + PasswordEncoder будуть використовуватися в AuthenticationManager
                .authenticationManager(authManager)

                // 5) додаємо наші JWT-фільтри у правильному порядку
                .addFilter(jwtAuthenticationFilter)
                .addFilterAfter(jwtAuthorizationFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}
