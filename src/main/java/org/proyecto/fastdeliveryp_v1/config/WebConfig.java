package org.proyecto.fastdeliveryp_v1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Inyección del valor de la ruta de las imágenes desde el archivo de propiedades
    @Value("${ruta.imagenes}")
    private String uploadDir;

    /**
     * Configura los manejadores de recursos estáticos.
     *
     * @param registry El registro de manejadores de recursos.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Manejar archivos subidos en el directorio configurado
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
