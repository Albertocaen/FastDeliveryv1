package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.entity.Producto;
import org.proyecto.fastdeliveryp_v1.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/inicio";
    }

    @GetMapping("/inicio")
    public String homePage(Model model) {
        List<Producto> productosDestacados = productoService.getProductosDestacados(3);
        List<Producto> todosLosProductos = productoService.getAllProductos();
        model.addAttribute("productosDestacados", productosDestacados);
        model.addAttribute("productos", todosLosProductos);
        return "home/index";
    }

    @PostMapping("/chat")
    @ResponseBody
    public Map<String, String> chat(@RequestBody Map<String, String> payload) {
        String userMessage = normalizeString(payload.get("message"));
        Map<String, String> response = new HashMap<>();

        // Respuestas predefinidas
        if (userMessage.contains("envio gratis")) {
            response.put("message", "El envío es gratis a partir de 50€.");
        } else if (userMessage.contains("horarios")) {
            response.put("message", "Nuestro horario de atención es de lunes a viernes de 9am a 6pm.");
        } else if (userMessage.contains("contacto")) {
            response.put("message", "Puedes contactarnos a través de nuestro email: contacto@ejemplo.com.");
        } else if (userMessage.contains("devolver un producto")) {
            response.put("message", "Para devolver un producto, por favor sigue las instrucciones en nuestra página de devoluciones.");
        } else if (userMessage.contains("estado de mi pedido")) {
            response.put("message", "Para conocer el estado de tu pedido, por favor ingresa a tu cuenta y verifica la sección de 'Mis Pedidos'.");
        } else {
            response.put("message", "Lo siento, no entiendo tu pregunta. Por favor, inténtalo de nuevo.");
        }

        return response;
    }

    private String normalizeString(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase();
    }
}