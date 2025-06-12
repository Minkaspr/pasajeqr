package com.mk.pasajeqr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configuración global de CORS
        registry.addMapping("/**")  // Especifica las rutas donde se habilita CORS
                .allowedOrigins("http://localhost:3000","https://pasajeqr-f.vercel.app")  // Origen permitido (frontend)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Métodos permitidos
                .allowedHeaders("*")  // Todos los encabezados permitidos
                .allowCredentials(true);  // Permite credenciales (cookies o tokens)
    }
}
