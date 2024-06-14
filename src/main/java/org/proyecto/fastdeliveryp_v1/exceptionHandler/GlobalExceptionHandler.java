package org.proyecto.fastdeliveryp_v1.exceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public String handleMultipartException(MultipartException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar la imagen. Solo se permiten archivos PNG y JPG.");
        return "redirect:/productos/new";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado. Por favor, inténtalo de nuevo.");
        return "redirect:/productos/new";
    }

}
