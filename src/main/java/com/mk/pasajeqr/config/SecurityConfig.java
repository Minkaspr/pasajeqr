package com.mk.pasajeqr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "https://pasajeqr-f.vercel.app"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /*
        http.securityMatcher("/**") // Aplica la configuración de seguridad a todas las rutas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/health",                          // Ruta abierta
                                "/api/v1/user",                     // Lista de usuarios
                                "/api/v1/user/"                     // Algunos servidores requieren con y sin slash
                        ).permitAll() // Permite el acceso sin autenticación a /health
                        .anyRequest().authenticated() // Requiere autenticación para cualquier otra ruta
                )
                .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF (útil en APIs REST)
                .cors(Customizer.withDefaults()); // Habilita CORS usando configuración por defecto o personalizada
        */
        http
                .securityMatcher("/**") // Aplica a todas las rutas
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // ¡Permite TODO sin autenticación!
                )
                .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF (ok para APIs REST)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())); // Usa configuración CORS

        return http.build(); // Construye y devuelve la cadena de filtros de seguridad
    }
}
