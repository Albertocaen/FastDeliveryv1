package org.proyecto.fastdeliveryp_v1.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
import org.springframework.validation.BindingResult;
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

    /**
     * Muestra la lista de productos con paginación.
     *
     * @param model     El modelo para pasar datos a la vista.
     * @param page      La página actual.
     * @param session   La sesión HTTP para obtener el carrito.
     * @param principal La información del usuario autenticado.
     * @return la vista de la lista de productos.
     */
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

    /**
     * Muestra el formulario para crear un nuevo producto.
     *
     * @param model El modelo para pasar datos a la vista.
     * @return la vista del formulario de nuevo producto.
     */
    @GetMapping("/new")
    public String newProductoForm(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/new :: new";
    }

    /**
     * Guarda un nuevo producto.
     *
     * @param producto           El producto a guardar.
     * @param imagen             El archivo de imagen del producto.
     * @param redirectAttributes Los atributos de redirección para pasar mensajes.
     * @return la redirección a la lista de productos.
     */
    @PostMapping
    public String saveProducto(@Valid @ModelAttribute Producto producto, BindingResult result,
                               @RequestParam("imagen") MultipartFile imagen,
                               RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "productos/new";
        }
        if (!imagen.isEmpty()) {
            String rutaImagen = guardarImagen(imagen);
            if ((rutaImagen !=null)){
                producto.setImg("uploads/" + rutaImagen); // Ruta relativa para acceder a la imagen
            }else {
                redirectAttributes.addFlashAttribute("errorMessage", "Formato de imagen incorrecto, Solo se permiten archivos PNG Y JPG");
                return "redirect:/productos/new";
            }

        }
        productoService.saveProducto(producto);
        redirectAttributes.addFlashAttribute("message", "Producto guardado con éxito");
        return "redirect:/productos";
    }

    /**
     * Muestra el formulario para editar un producto existente.
     *
     * @param id    El ID del producto a editar.
     * @param model El modelo para pasar datos a la vista.
     * @return la vista del formulario de edición de producto.
     */
    @GetMapping("/edit/{id}")
    public String editProductoForm( @Valid @PathVariable Integer id, Model model) {
        Producto producto = productoService.getProductoById(id);
        model.addAttribute("producto", producto);
        return "productos/edit :: edit";
    }

    /**
     * Actualiza un producto existente.
     *
     * @param producto           El producto con los datos actualizados.
     * @param imagen             El archivo de imagen del producto.
     * @param redirectAttributes Los atributos de redirección para pasar mensajes.
     * @return la redirección a la lista de productos.
     */
    @PostMapping("/update")
    public String updateProducto(@ModelAttribute Producto producto,BindingResult result,
                                 @RequestParam("imagen") MultipartFile imagen,
                                 RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "productos/edit";
        }
        if (!imagen.isEmpty()) {
            String rutaImagen = guardarImagen(imagen);
            if ((rutaImagen !=null)){
                producto.setImg("uploads/" + rutaImagen); // Ruta relativa para acceder a la imagen
            }else {
                redirectAttributes.addFlashAttribute("errorMessage","Formato de imagen incorrecto, Solo se permiten archivos Y JPG");
            }

        }
        productoService.saveProducto(producto);
        redirectAttributes.addFlashAttribute("message", "Producto actualizado con éxito");
        return "redirect:/productos";
    }

    /**
     * Guarda la imagen en el sistema de archivos.
     *
     * @param imagen El archivo de imagen a guardar.
     * @return El nombre del archivo guardado.
     */

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

    /**
     * Elimina un producto por su ID.
     *
     * @param id                 El ID del producto a eliminar.
     * @param redirectAttributes Los atributos de redirección para pasar mensajes.
     * @return la redirección a la lista de productos.
     */
    @GetMapping("/delete/{id}")
    public String deleteProducto(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        productoService.deleteProducto(id);
        redirectAttributes.addFlashAttribute("message", "Producto eliminado con éxito");
        return "redirect:/productos";
    }
}
