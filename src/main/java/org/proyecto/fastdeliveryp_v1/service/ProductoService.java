package org.proyecto.fastdeliveryp_v1.service;

import org.proyecto.fastdeliveryp_v1.entity.Producto;
import org.proyecto.fastdeliveryp_v1.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


import java.util.List;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

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
}
