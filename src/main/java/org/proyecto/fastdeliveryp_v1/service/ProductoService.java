package org.proyecto.fastdeliveryp_v1.service;



import jakarta.servlet.http.HttpSession;
import org.proyecto.fastdeliveryp_v1.dto.PersonaDto;
import org.proyecto.fastdeliveryp_v1.entity.CarritoItem;
import org.proyecto.fastdeliveryp_v1.entity.Producto;
import org.proyecto.fastdeliveryp_v1.mapper.ProductoMapper;
import org.proyecto.fastdeliveryp_v1.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.ArrayList;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    ProductoMapper mapper;


    public Page<Producto> getAllProductos(Pageable pageable) {
        return productoRepository.findAll(pageable);
    }

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Producto getProductoById(Integer id) {
        return productoRepository.findById(id).orElse(null);
    }

    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public void deleteProducto(Integer id) {
        productoRepository.deleteById(id);
    }

    public List<Producto> getProductosDestacados(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return productoRepository.findTopProductos(pageable);
    }

    public List<CarritoItem> obtenerCarritoDesdeSesion(HttpSession session) {
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    public void agregarProductoAlCarrito(HttpSession session, Integer idProducto, Integer cantidad) {
        List<CarritoItem> carrito = obtenerCarritoDesdeSesion(session);
        Producto producto = getProductoById(idProducto);
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
    }

    public void eliminarProductoDelCarrito(HttpSession session, Integer idProducto) {
        List<CarritoItem> carrito = obtenerCarritoDesdeSesion(session);
        carrito.removeIf(item -> item.getProducto().getId().equals(idProducto));
    }


    public static class CarritoResponse {
        private int itemsCount;

        public CarritoResponse(int itemsCount) {
            this.itemsCount = itemsCount;
        }

        public int getItemsCount() {
            return itemsCount;
        }

        public void setItemsCount(int itemsCount) {
            this.itemsCount = itemsCount;
        }
    }


    public List<PersonaDto> findAll() {
        return productoRepository.findAll().stream().map(mapper::toDto).collect(java.util.stream.Collectors.toList());
    }
    public PersonaDto findById(Integer id) {
        return productoRepository.findById(id).map(mapper::toDto).orElse(null);
    }

    @PostMapping
    public PersonaDto save(PersonaDto dto) {
        Producto producto = mapper.toEntity(dto);
        Producto savedProducto  = productoRepository.save(producto);
        return mapper.toDto(savedProducto);
    }

    public void deleteById(Integer id) {
        productoRepository.deleteById(id);
    }
}
