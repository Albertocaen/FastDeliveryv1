package org.proyecto.fastdeliveryp_v1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configura el broker de mensajes.
     *
     * @param config El registro de configuración del broker de mensajes.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilita un broker simple con el prefijo /topic para los mensajes entrantes
        config.enableSimpleBroker("/topic");
        // Establece el prefijo /app para los destinos de mensajes de la aplicación
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registra los endpoints STOMP que los clientes usarán para conectarse al servidor WebSocket.
     *
     * @param registry El registro de endpoints STOMP.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registra un endpoint WebSocket en la ruta /ws y permite el uso de SockJS
        registry.addEndpoint("/ws").setAllowedOrigins("http://fastdeliveryapp.eastus.azurecontainer.io:8080")
                .withSockJS();
    }
}
