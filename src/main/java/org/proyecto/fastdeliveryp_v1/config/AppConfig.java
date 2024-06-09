package org.proyecto.fastdeliveryp_v1.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
@Configuration
public class AppConfig {


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, CookieInterceptor cookieInterceptor) {
        return builder
                .additionalInterceptors(Collections.singletonList(cookieInterceptor))
                .build();
    }
}
