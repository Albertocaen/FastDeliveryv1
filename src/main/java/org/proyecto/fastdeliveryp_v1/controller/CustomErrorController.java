package org.proyecto.fastdeliveryp_v1.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador personalizado para manejar errores globales de la aplicación.
 * Implementa la interfaz {@link ErrorController} para proporcionar manejo de errores personalizados.
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Maneja las solicitudes que resultan en un error.
     *
     * @param request la solicitud HTTP que produjo el error.
     * @param model   el modelo para añadir atributos que serán usados en la vista.
     * @return la vista correspondiente al código de error.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            if (statusCode == 404) {
                model.addAttribute("errorMessage", "Lo sentimos, la página que buscas no existe.");
                return "error/404";
            } else if (statusCode == 403) {
                model.addAttribute("errorMessage", "Acceso denegado: No tienes permiso para entrar en esta dirección.");
                return "error/403";
            }
        }
        model.addAttribute("errorMessage", "Ocurrió un error inesperado. Por favor, inténtalo de nuevo.");
        return "error/generico";
    }
}