package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.entity.Producto;
import org.proyecto.fastdeliveryp_v1.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String listProductos(Model model) {
        List<Producto> productos = productoService.getAllProductos();
        model.addAttribute("productos", productos);
        return "productos/list";
    }

    @GetMapping("/new")
    public String newProductoForm(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/new";
    }

    @PostMapping
    public String saveProducto(@ModelAttribute Producto producto) {
        productoService.saveProducto(producto);
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String editProductoForm(@PathVariable Integer id, Model model) {
        Producto producto = productoService.getProductoById(id);
        model.addAttribute("producto", producto);
        return "productos/edit";
    }

    @PostMapping("/update")
    public String updateProducto(@ModelAttribute Producto producto) {
        productoService.saveProducto(producto);
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String deleteProducto(@PathVariable Integer id) {
        productoService.deleteProducto(id);
        return "redirect:/productos";
    }
}
