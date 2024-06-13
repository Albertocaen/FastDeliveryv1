package org.proyecto.fastdeliveryp_v1.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieInterceptor implements ClientHttpRequestInterceptor {

    private final HttpServletRequest request;

    /**
     * Constructor para inyectar la solicitud HTTP.
     *
     * @param request La solicitud HTTP actual.
     */

    public CookieInterceptor(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Método que intercepta la solicitud HTTP y añade la cookie JWT al encabezado si está presente.
     *
     * @param request   La solicitud HTTP.
     * @param body      El cuerpo de la solicitud HTTP.
     * @param execution Ejecución de la solicitud HTTP.
     * @return La respuesta HTTP.
     * @throws IOException Si ocurre un error durante la ejecución de la solicitud.
     */

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // Obtiene todas las cookies de la solicitud
        Cookie[] cookies = this.request.getCookies();

        // Busca la cookie JWT en el arreglo de cookies
        Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                .filter(cookie -> "JWT".equals(cookie.getName()))
                .findFirst();
        // Si la cookie JWT está presente, la añade a los encabezados de la solicitud
        jwtCookie.ifPresent(cookie -> request.getHeaders().add("Cookie", "JWT=" + cookie.getValue()));
        // Ejecuta la solicitud HTTP con los cambios realizados
        return execution.execute(request, body);
    }
}
