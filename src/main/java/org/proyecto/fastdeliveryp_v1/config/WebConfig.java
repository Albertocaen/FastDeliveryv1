package org.proyecto.fastdeliveryp_v1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${ruta.imagenes}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");

        // Manejar archivos en la carpeta static
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");


        // Para servir fragmentos en la carpeta templates/fragmentos
        registry.addResourceHandler("/templates/**")
                .addResourceLocations("classpath:/templates/");
    }


}
