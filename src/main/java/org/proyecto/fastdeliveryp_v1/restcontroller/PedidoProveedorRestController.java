package org.proyecto.fastdeliveryp_v1.restcontroller;

import java.lang.Long;
import java.util.List;
import org.proyecto.fastdeliveryp_v1.dto.PedidoProveedorDto;
import org.proyecto.fastdeliveryp_v1.service.PedidoProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidoproveedors")
public class PedidoProveedorRestController {
  @Autowired
  private PedidoProveedorService service;

  @GetMapping
  public List<PedidoProveedorDto> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public PedidoProveedorDto findById(Integer id) {
    return service.findById(id);
  }

  @PostMapping
  public PedidoProveedorDto save(PedidoProveedorDto dto) {
    return service.save(dto);
  }

  @DeleteMapping("/{id}")
  public void deleteById(Integer id) {
    service.deleteById(id);
  }
}
