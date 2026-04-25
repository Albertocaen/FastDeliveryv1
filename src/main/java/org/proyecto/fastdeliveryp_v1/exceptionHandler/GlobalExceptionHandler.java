package org.proyecto.fastdeliveryp_v1.exceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handler global de excepciones lanzadas por controllers MVC.
 *
 * <p>Cualquier excepción no capturada en los controllers cae aquí. Cada
 * {@code @ExceptionHandler} mapea un tipo concreto a una vista de error
 * (o redirección con flash message).</p>
 *
 * <p><b>Nota de auditoría 2026-04:</b> el import previo era
 * {@code java.nio.file.AccessDeniedException}, que jamás es lanzada por Spring
 * Security. El import correcto es {@code org.springframework.security.access.AccessDeniedException}.
 * Sin este fix, los 403 reales caían al handler genérico {@code Exception}.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Errores subiendo archivos (tamaño excedido, tipo no permitido, etc.).
     * Redirige al alta de productos porque es ahí donde más se suben imágenes.
     *
     * @param ex                 la excepción lanzada por el resolver de multipart.
     * @param redirectAttributes atributos para flash message en la siguiente vista.
     * @return redirect al formulario de alta de producto con mensaje de error.
     */
    @ExceptionHandler(MultipartException.class)
    public String handleMultipartException(MultipartException ex,
                                           RedirectAttributes redirectAttributes) {
        log.warn("Error al cargar archivo multipart", ex);
        redirectAttributes.addFlashAttribute(
                "errorMessage",
                "Error al cargar la imagen. Solo se permiten archivos PNG y JPG."
        );
        return "redirect:/productos/new";
    }

    /**
     * Página no encontrada (cuando Spring no resuelve el handler).
     * Requiere {@code spring.mvc.throw-exception-if-no-handler-found=true} y
     * {@code spring.web.resources.add-mappings=false} en {@code application.properties}
     * para que esta excepción se propague aquí.
     *
     * @param ex    la excepción de no-handler-found.
     * @param model modelo para añadir el mensaje de error a la vista.
     * @return vista 404.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundException(NoHandlerFoundException ex, Model model) {
        log.info("Página no encontrada: {}", ex.getRequestURL());
        model.addAttribute("errorMessage", "Lo sentimos, la página que buscas no existe.");
        return "error/404";
    }

    /**
     * Acceso denegado por Spring Security (rol insuficiente, sin sesión válida,
     * etc.). Es CRÍTICO importar {@code org.springframework.security.access.AccessDeniedException}
     * y no {@code java.nio.file.AccessDeniedException}.
     *
     * @param ex    la excepción de Spring Security.
     * @param model modelo para añadir el mensaje a la vista.
     * @return vista 403.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model) {
        log.info("Acceso denegado: {}", ex.getMessage());
        model.addAttribute(
                "errorMessage",
                "Acceso denegado: no tienes permiso para entrar en esta dirección."
        );
        return "error/403";
    }

    /**
     * Catch-all para cualquier excepción no manejada arriba.
     *
     * <p>Antes redirigía siempre a {@code /productos/new}, lo cual era incorrecto:
     * un fallo en {@code /carrito} acababa enviándote al alta de producto. Ahora
     * renderiza una vista de error genérica que no asume contexto.</p>
     *
     * @param ex    la excepción no controlada.
     * @param model modelo para añadir el mensaje a la vista.
     * @return vista de error genérica.
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        log.error("Error inesperado en el controller", ex);
        model.addAttribute(
                "errorMessage",
                "Ocurrió un error inesperado. Por favor, inténtalo de nuevo en unos instantes."
        );
        return "error/generico";
    }
}
