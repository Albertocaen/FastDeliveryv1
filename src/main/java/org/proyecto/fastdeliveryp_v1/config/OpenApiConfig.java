package org.proyecto.fastdeliveryp_v1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Metadatos globales de la especificación OpenAPI 3.
 *
 * <p>La UI de Swagger se sirve automáticamente en {@code /swagger-ui/index.html}
 * y el JSON de la spec en {@code /v3/api-docs}.</p>
 *
 * <p>Los endpoints documentados se filtran por {@code springdoc.paths-to-match}
 * definido en {@code application.properties}.</p>
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bacoDeliveryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BacoDelivery API")
                        .description("""
                                API REST de **FastDelivery · Baco** — aplicación de gestión de pedidos
                                y reparto a domicilio.

                                Cubre: autenticación JWT, catálogo de productos, gestión de pedidos,
                                panel de administración, stock y vehículos de reparto.

                                > La autenticación se realiza mediante cookie `jwt` (HttpOnly).
                                > Inicia sesión en `/login` para obtener la cookie y poder probar
                                > los endpoints protegidos desde Swagger UI.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Alberto Caen")
                                .email("alberto.caen.1@gmail.com")
                                .url("https://github.com/Albertocaen"))
                        .license(new License()
                                .name("Proyecto personal · Portfolio")
                                .url("https://bacodelivery.com/sobre-mi")))
                .servers(List.of(
                        new Server()
                                .url("https://bacodelivery.com")
                                .description("Producción"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local")));
    }
}
