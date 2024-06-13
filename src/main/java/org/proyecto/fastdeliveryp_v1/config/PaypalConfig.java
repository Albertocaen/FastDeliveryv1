package org.proyecto.fastdeliveryp_v1.config;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaypalConfig {

    // Inyección del valor del client ID desde el archivo de propiedades
    @Value("${paypal.client.id}")
    private String clientId;

    // Inyección del valor del client secret desde el archivo de propiedades
    @Value("${paypal.client.secret}")
    private String clientSecret;

    // Inyección del valor del modo (sandbox/live) desde el archivo de propiedades
    @Value("${paypal.mode}")
    private String mode;

    /**
     * Configura los parámetros del SDK de PayPal.
     *
     * @return un mapa con la configuración del SDK de PayPal.
     */
    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);
        return configMap;
    }

    /**
     * Proporciona las credenciales OAuth para la autenticación con PayPal.
     *
     * @return una instancia de OAuthTokenCredential.
     */
    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    /**
     * Configura y proporciona el contexto de la API de PayPal.
     *
     * @return una instancia configurada de APIContext.
     * @throws PayPalRESTException si ocurre un error al obtener el token de acceso.
     */
    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
        context.setConfigurationMap(paypalSdkConfig());
        return context;
    }
}
