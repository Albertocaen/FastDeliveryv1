package org.proyecto.fastdeliveryp_v1.restcontroller;

import java.lang.Long;
import java.util.List;
import org.proyecto.fastdeliveryp_v1.dto.CocheDto;
import org.proyecto.fastdeliveryp_v1.service.CocheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coches")
public class CocheRestController {
  @Autowired
  private CocheService service;

  @GetMapping
  public List<CocheDto> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public CocheDto findById(Long id) {
    return service.findById(id);
  }

  @PostMapping
  public CocheDto save(CocheDto dto) {
    return service.save(dto);
  }

  @DeleteMapping("/{id}")
  public void deleteById(Long id) {
    service.deleteById(id);
  }
}
