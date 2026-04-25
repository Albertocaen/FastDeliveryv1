package org.proyecto.fastdeliveryp_v1.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Propaga la cookie {@code JWT} del usuario al hacer llamadas {@code RestTemplate}
 * salientes desde el backend.
 *
 * <p>Cuando el controller MVC llama a su propio API REST interno (por ejemplo, para
 * delegar listados ya construidos), este interceptor copia la cookie JWT del usuario
 * actual al header {@code Cookie} del request saliente, para que el endpoint REST
 * vea al mismo usuario autenticado.</p>
 *
 * <p><b>Auditoría 2026-04 — fix:</b> {@code request.getCookies()} puede devolver
 * {@code null} (si la petición no trae cookies). El código previo asumía no-null y
 * lanzaba NPE en ese caso. Ahora se maneja con guarda explícita.</p>
 */
@Component
public class CookieInterceptor implements ClientHttpRequestInterceptor {

    private static final String JWT_COOKIE_NAME = "JWT";

    private final HttpServletRequest request;

    /**
     * @param request la petición HTTP entrante (inyectada por Spring para acceder
     *                a las cookies del usuario actual).
     */
    public CookieInterceptor(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Intercepta el request saliente y copia la cookie JWT al header {@code Cookie}.
     *
     * @param request   request saliente.
     * @param body      cuerpo del request.
     * @param execution cadena de ejecución para continuar.
     * @return la respuesta del servidor remoto.
     * @throws IOException si la ejecución falla.
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        Cookie[] cookies = this.request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> JWT_COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();
            jwtCookie.ifPresent(cookie ->
                    request.getHeaders().add("Cookie", JWT_COOKIE_NAME + "=" + cookie.getValue()));
        }
        return execution.execute(request, body);
    }
}
