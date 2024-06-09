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

    public CookieInterceptor(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        Cookie[] cookies = this.request.getCookies();
        Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                .filter(cookie -> "JWT".equals(cookie.getName()))
                .findFirst();

        jwtCookie.ifPresent(cookie -> request.getHeaders().add("Cookie", "JWT=" + cookie.getValue()));

        return execution.execute(request, body);
    }
}
