package org.proyecto.fastdeliveryp_v1.controller;

import jakarta.servlet.http.HttpSession;
import org.proyecto.fastdeliveryp_v1.entity.CarritoItem;
import org.proyecto.fastdeliveryp_v1.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    /**
     * Muestra la vista del carrito de compras.
     * @param model El modelo para pasar datos a la vista.
     * @param session La sesión HTTP para obtener el carrito.
     * @return la vista del carrito.
     */
    @GetMapping
    public String verCarrito(Model model, HttpSession session) {
        List<CarritoItem> carrito = productoService.obtenerCarritoDesdeSesion(session);
        double totalCarrito = carrito.stream().mapToDouble(CarritoItem::getTotal).sum();
        model.addAttribute("carrito", carrito);
        model.addAttribute("totalCarrito", String.format("%.2f", totalCarrito));
        return "carrito/ver";
    }

    /**
     * Agrega un producto al carrito de compras.
     * @param idProducto El ID del producto a agregar.
     * @param cantidad La cantidad del producto a agregar.
     * @param session La sesión HTTP para obtener el carrito.
     * @return La respuesta con el tamaño del carrito.
     */
    @PostMapping("/agregar")
    @ResponseBody
    public ProductoService.CarritoResponse agregarAlCarrito(@RequestParam("idProducto") Integer idProducto,
                                                            @RequestParam("cantidad") Integer cantidad,
                                                            HttpSession session) {
        productoService.agregarProductoAlCarrito(session, idProducto, cantidad);
        List<CarritoItem> carrito = productoService.obtenerCarritoDesdeSesion(session);
        return new ProductoService.CarritoResponse(carrito.size());
    }

    /**
     * Elimina un producto del carrito de compras.
     * @param idProducto El ID del producto a eliminar.
     * @param session La sesión HTTP para obtener el carrito.
     * @return Redirección a la vista del carrito.
     */
    @PostMapping("/eliminar")
    public String eliminarDelCarrito(@RequestParam("idProducto") Integer idProducto, HttpSession session) {
        productoService.eliminarProductoDelCarrito(session, idProducto);
        return "redirect:/carrito";
    }
}
