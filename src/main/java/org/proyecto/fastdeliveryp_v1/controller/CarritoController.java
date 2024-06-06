package org.proyecto.fastdeliveryp_v1.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.proyecto.fastdeliveryp_v1.entity.CarritoItem;
import org.proyecto.fastdeliveryp_v1.entity.Producto;
import org.proyecto.fastdeliveryp_v1.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    private List<CarritoItem> obtenerCarritoDesdeCookie(String carritoCookie) {
        return productoService.obtenerCarritoDesdeCookie(carritoCookie);
    }

    private String generarCookieDesdeCarrito(List<CarritoItem> carrito) {
        return productoService.generarCookieDesdeCarrito(carrito);
    }

    @GetMapping
    public String verCarrito(Model model, @CookieValue(value = "carrito", defaultValue = "") String carritoCookie) {
        List<CarritoItem> carrito = obtenerCarritoDesdeCookie(carritoCookie);
        double totalCarrito = carrito.stream().mapToDouble(CarritoItem::getTotal).sum();
        model.addAttribute("carrito", carrito);
        model.addAttribute("totalCarrito", String.format("%.2f", totalCarrito));
        return "carrito/ver";
    }

    @PostMapping("/agregar")
    @ResponseBody
    public ProductoService.CarritoResponse agregarAlCarrito(@RequestParam("idProducto") Integer idProducto,
                                                            @RequestParam("cantidad") Integer cantidad,
                                                            @CookieValue(value = "carrito", defaultValue = "") String carritoCookie,
                                                            HttpServletResponse response) {
        List<CarritoItem> carrito = obtenerCarritoDesdeCookie(carritoCookie);

        Producto producto = productoService.getProductoById(idProducto);
        if (producto != null) {
            boolean productoExistente = false;
            for (CarritoItem item : carrito) {
                if (item.getProducto().getId().equals(idProducto)) {
                    item.setCantidad(item.getCantidad() + cantidad);
                    productoExistente = true;
                    break;
                }
            }
            if (!productoExistente) {
                carrito.add(new CarritoItem(producto, cantidad));
            }
        }

        String nuevaCookie = generarCookieDesdeCarrito(carrito);
        Cookie cookie = new Cookie("carrito", nuevaCookie);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        return new ProductoService.CarritoResponse(carrito.size());
    }

    @PostMapping("/eliminar")
    public String eliminarDelCarrito(@RequestParam("idProducto") Integer idProducto,
                                     @CookieValue(value = "carrito", defaultValue = "") String carritoCookie,
                                     HttpServletResponse response) {
        List<CarritoItem> carrito = obtenerCarritoDesdeCookie(carritoCookie);
        carrito.removeIf(item -> item.getProducto().getId().equals(idProducto));

        String nuevaCookie = generarCookieDesdeCarrito(carrito);
        Cookie cookie = new Cookie("carrito", nuevaCookie);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        return "redirect:/carrito";
    }
}
