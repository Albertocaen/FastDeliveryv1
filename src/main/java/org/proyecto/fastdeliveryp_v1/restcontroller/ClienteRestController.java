package org.proyecto.fastdeliveryp_v1.restcontroller;

import java.lang.Long;
import java.util.List;
import org.proyecto.fastdeliveryp_v1.dto.ClienteDto;
import org.proyecto.fastdeliveryp_v1.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {
  @Autowired
  private ClienteService service;

  @GetMapping("/all")
  public List<ClienteDto> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ClienteDto findById(String id) {
    return service.findById(id);
  }

  @PostMapping
  public ClienteDto save(ClienteDto dto) {
    return service.save(dto);
  }

  @DeleteMapping("/{id}")
  public void deleteById(String id) {
    service.deleteById(id);
  }
}
