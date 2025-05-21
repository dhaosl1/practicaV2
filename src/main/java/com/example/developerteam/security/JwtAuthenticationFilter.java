package com.example.developerteam.security;

import com.example.developerteam.service.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        setFilterProcessesUrl("/auth/login"); // endpoint для логіну
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        try {
            Map<String, String> creds = new ObjectMapper()
                    .readValue(request.getInputStream(), Map.class);

            String username = creds.get("username");
            String password = creds.get("password");

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException | java.io.IOException ex) {
            throw new RuntimeException("Invalid login request");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws java.io.IOException, ServletException {

        CustomUserDetails userPrincipal = (CustomUserDetails) authResult.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authResult);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Повернемо JSON з токеном
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", jwt);
        tokenMap.put("username", userPrincipal.getUsername());

        new ObjectMapper().writeValue(response.getWriter(), tokenMap);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws java.io.IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Unauthorized: " + failed.getMessage());
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), error);
    }
}
