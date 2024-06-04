package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.entity.Producto;
import org.proyecto.fastdeliveryp_v1.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Value("${ruta.imagenes}")
    private String rutaImagenes;

    @GetMapping
    public String listProductos(Model model, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 10; // Tamaño de la página

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Producto> productoPage = productoService.getAllProductos(pageable);

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
