package org.proyecto.fastdeliveryp_v1.restcontroller;

import java.util.List;

import org.proyecto.fastdeliveryp_v1.dto.PedidoProveedorDto;
import org.proyecto.fastdeliveryp_v1.service.PedidoProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidoproveedores")
public class PedidoProveedorRestController {

    @Autowired
    private PedidoProveedorService service;

    @GetMapping
    public List<PedidoProveedorDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PedidoProveedorDto findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public PedidoProveedorDto save(@RequestBody PedidoProveedorDto dto) {
        PedidoProveedorDto savedPedido = service.save(dto);
        service.saveProductos(dto.getProductos(), savedPedido.getId());
        return savedPedido;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id) {
        service.deleteById(id);
    }
}


