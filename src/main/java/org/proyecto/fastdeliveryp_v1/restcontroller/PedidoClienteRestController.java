package org.proyecto.fastdeliveryp_v1.restcontroller;

import java.lang.Long;
import java.util.List;
import org.proyecto.fastdeliveryp_v1.dto.PedidoClienteDto;
import org.proyecto.fastdeliveryp_v1.service.PedidoClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidoclientes")
public class PedidoClienteRestController {
  @Autowired
  private PedidoClienteService service;

  @GetMapping
  public List<PedidoClienteDto> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public PedidoClienteDto findById(Integer id) {
    return service.findById(id);
  }

  @PostMapping
  public PedidoClienteDto save(PedidoClienteDto dto) {
    return service.save(dto);
  }

  @DeleteMapping("/{id}")
  public void deleteById(Integer id) {
    service.deleteById(id);
  }
}
