package org.proyecto.fastdeliveryp_v1.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    /**
     * Maneja los mensajes enviados al endpoint "/notify" y los reenvía al tópico "/topic/notifications".
     *
     * @param message El mensaje recibido.
     * @return El mensaje a enviar a los suscriptores del tópico "/topic/notifications".
     */
    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public String sendNotification(String message) {
        return message;
    }

}