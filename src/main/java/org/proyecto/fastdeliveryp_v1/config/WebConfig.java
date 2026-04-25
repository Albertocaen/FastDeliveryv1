package org.proyecto.fastdeliveryp_v1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de recursos estáticos.
 *
 * <p>Expone dos rutas de recursos:</p>
 * <ul>
 *   <li><b>/uploads/**</b> — sirve imágenes subidas por usuarios desde el directorio
 *       configurado en {@code ruta.imagenes}. Es necesario para mostrar fotos de
 *       producto y avatares cargados en runtime.</li>
 *   <li><b>/static/**</b> — sirve assets del classpath (CSS, JS, imágenes incluidas
 *       en el JAR). Es la ruta que usa Thymeleaf con {@code th:src="/static/..."}.</li>
 * </ul>
 *
 * <p><b>Nota de seguridad (auditoría 2026-04):</b> previamente este config exponía
 * {@code /templates/**} hacia el classpath, lo que permitía a cualquier visitante
 * pedir <code>/templates/fragmentos/admin-panel.html</code> y leer la estructura
 * interna de la app. Eliminado. Los fragmentos cargados dinámicamente se sirven
 * desde endpoints {@code @GetMapping} que devuelven el fragmento Thymeleaf
 * con notación {@code "ruta/plantilla :: fragmento"}.</p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /** Directorio del filesystem donde se almacenan las imágenes subidas. */
    @Value("${ruta.imagenes}")
    private String uploadDir;

    /**
     * Registra los manejadores de recursos estáticos públicos.
     *
     * @param registry registro de manejadores de recursos de Spring MVC.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Imágenes subidas por usuarios (catálogo de productos, avatares).
        // Sirve archivos del filesystem; el path debe existir y tener permisos de
        // lectura para el usuario del proceso Java.
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");

        // Assets estáticos del classpath: CSS, JS, imágenes empaquetadas en el JAR.
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // Eliminado deliberadamente: registry.addResourceHandler("/templates/**")
        // Razón: exponía cualquier fragmento HTML a peticiones públicas.
        // Los fragmentos dinámicos van por endpoints controlados.
    }
}
