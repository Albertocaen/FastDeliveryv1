package org.proyecto.fastdeliveryp_v1.test;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.proyecto.fastdeliveryp_v1.controller.CarritoController;
import org.proyecto.fastdeliveryp_v1.entity.Producto;
import org.proyecto.fastdeliveryp_v1.security.JwtTokenUtil;
import org.proyecto.fastdeliveryp_v1.security.SecurityConfig;
import org.proyecto.fastdeliveryp_v1.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CarritoController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
public class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;
// Test para comprobar que agerga en carrito basandose en la cookie , se implemento el jwtoken util para poder validar al igual que el roles
    @Test
    @WithMockUser(roles = "USER")
    public void testAgregarAlCarrito() throws Exception {
        Producto producto = new Producto();
        producto.setId(1);
        when(productoService.getProductoById(anyInt())).thenReturn(producto);

        mockMvc.perform(post("/carrito/agregar")
                        .param("idProducto", "1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
    // Test para comprobar que elimina en carrito basandose en la cookie , se implemento el jwtoken util para poder validar al igual que el roles
    @Test
    @WithMockUser(roles = "USER")
    public void testEliminarDelCarrito() throws Exception {
        Producto producto = new Producto();
        producto.setId(1);
        when(productoService.getProductoById(anyInt())).thenReturn(producto);

        mockMvc.perform(post("/carrito/eliminar")
                        .param("idProducto", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }
}
