package org.proyecto.fastdeliveryp_v1.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.proyecto.fastdeliveryp_v1.entity.Producto;
import org.proyecto.fastdeliveryp_v1.repository.ProductoRepository;
import org.proyecto.fastdeliveryp_v1.service.ProductoService;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        producto1 = new Producto();
        producto1.setId(1);
        producto1.setNombre("Producto 1");
        producto2 = new Producto();
        producto2.setId(2);
        producto2.setNombre("Producto 2");
    }

    @Test
    void testGetAllProductos() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto1, producto2));

        List<Producto> productos = productoService.getAllProductos();
        assertEquals(2, productos.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testGetProductoById() {
        when(productoRepository.findById(anyInt())).thenReturn(Optional.of(producto1));

        Producto producto = productoService.getProductoById(1);
        assertNotNull(producto);
        assertEquals("Producto 1", producto.getNombre());
        verify(productoRepository, times(1)).findById(1);
    }

}
