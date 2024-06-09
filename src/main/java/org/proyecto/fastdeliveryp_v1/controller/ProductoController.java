package org.proyecto.fastdeliveryp_v1.controller;

import jakarta.servlet.http.HttpSession;
import org.proyecto.fastdeliveryp_v1.entity.CarritoItem;
import org.proyecto.fastdeliveryp_v1.entity.Producto;
import org.proyecto.fastdeliveryp_v1.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Value("${ruta.imagenes}")
    private String rutaImagenes;


    @GetMapping
    public String listProductos(Model model, @RequestParam(defaultValue = "0") int page, HttpSession session, Principal principal) {
        String userId = principal != null ? principal.getName() : "anonimo";
        userId = userId.replaceAll("[^a-zA-Z0-9]", "_");

        int pageSize = 6; // Tamaño de la página
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Producto> productoPage = productoService.getAllProductos(pageable);

        if (page >= productoPage.getTotalPages() && productoPage.getTotalPages() > 0) {
            return "redirect:/productos?page=" + (productoPage.getTotalPages() - 1);
        }

        List<CarritoItem> carrito = productoService.obtenerCarritoDesdeSesion(session);
        model.addAttribute("carrito", carrito);
        model.addAttribute("carritoItemsCount", carrito.size());
        model.addAttribute("productos", productoPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productoPage.getTotalPages());

        return "productos/list";
    }


    @GetMapping("/new")
    public String newProductoForm(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/new";
    }

    @PostMapping
    public String saveProducto(@ModelAttribute Producto producto,
                               @RequestParam("imagen") MultipartFile imagen,
                               RedirectAttributes redirectAttributes) {
        if (!imagen.isEmpty()) {
            String rutaImagen = guardarImagen(imagen);
            producto.setImg("uploads/" + rutaImagen); // Ruta relativa para acceder a la imagen
        }
        productoService.saveProducto(producto);
        redirectAttributes.addFlashAttribute("message", "Producto guardado con éxito");
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String editProductoForm(@PathVariable Integer id, Model model) {
        Producto producto = productoService.getProductoById(id);
        model.addAttribute("producto", producto);
        return "productos/edit";
    }

    @PostMapping("/update")
    public String updateProducto(@ModelAttribute Producto producto,
                                 @RequestParam("imagen") MultipartFile imagen,
                                 RedirectAttributes redirectAttributes) {
        if (!imagen.isEmpty()) {
            String rutaImagen = guardarImagen(imagen);
            producto.setImg("uploads/" + rutaImagen); // Ruta relativa para acceder a la imagen
        }
        productoService.saveProducto(producto);
        redirectAttributes.addFlashAttribute("message", "Producto actualizado con éxito");
        return "redirect:/productos";
    }

    private String guardarImagen(MultipartFile imagen) {
        try {
            String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
            Path rutaArchivo = Paths.get(rutaImagenes, nombreArchivo);
            Files.copy(imagen.getInputStream(), rutaArchivo);
            return nombreArchivo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProducto(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        productoService.deleteProducto(id);
        redirectAttributes.addFlashAttribute("message", "Producto eliminado con éxito");
        return "redirect:/productos";
    }
}
