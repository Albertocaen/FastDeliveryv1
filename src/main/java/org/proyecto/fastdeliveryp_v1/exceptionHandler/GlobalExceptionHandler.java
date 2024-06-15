package org.proyecto.fastdeliveryp_v1.exceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de carga de archivos multipart (errores de carga).
     *
     * @param e la excepción de carga multipart.
     * @param redirectAttributes los atributos para redirigir con mensajes flash.
     * @return la redirección a la página de creación de productos con un mensaje de error.
     */
    @ExceptionHandler(MultipartException.class)
    public String handleMultipartException(MultipartException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar la imagen. Solo se permiten archivos PNG y JPG.");
        return "redirect:/productos/new";
    }

    /**
     * Maneja las excepciones generales.
     *
     * @param e la excepción general.
     * @param redirectAttributes los atributos para redirigir con mensajes flash.
     * @return la redirección a la página de creación de productos con un mensaje de error.
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado. Por favor, inténtalo de nuevo.");
        return "redirect:/productos/new";
    }
    /**
     * Maneja las excepciones de acceso denegado (404).
     *
     * @param ex la excepción de acceso denegado.
     * @param model el modelo para añadir atributos.
     * @return la vista de error 404.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundException(NoHandlerFoundException ex, Model model) {
        model.addAttribute("errorMessage", "Lo sentimos, la página que buscas no existe.");
        return "error/404";
    }

    /**
     * Maneja las excepciones de acceso denegado (403).
     *
     * @param ex la excepción de acceso denegado.
     * @param model el modelo para añadir atributos.
     * @return la vista de error 403.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model) {
        model.addAttribute("errorMessage", "Acceso denegado: No tienes permiso para entrar en esta dirreción");
        return "error/403";
    }

}
