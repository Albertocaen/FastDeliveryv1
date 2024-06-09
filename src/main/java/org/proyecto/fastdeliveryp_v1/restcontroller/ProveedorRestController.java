package org.proyecto.fastdeliveryp_v1.restcontroller;

import java.lang.Long;
import java.util.List;
import org.proyecto.fastdeliveryp_v1.dto.ProveedorDto;
import org.proyecto.fastdeliveryp_v1.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorRestController {
  @Autowired
  private ProveedorService service;

  @GetMapping
  public List<ProveedorDto> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ProveedorDto findById(Integer id) {
    return service.findById(id);
  }

  @PostMapping
  public ProveedorDto save(ProveedorDto dto) {
    return service.save(dto);
  }

  @DeleteMapping("/{id}")
  public void deleteById(Integer id) {
    service.deleteById(id);
  }
}
