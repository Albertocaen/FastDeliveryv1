package org.proyecto.fastdeliveryp_v1.restcontroller;

import java.lang.Long;
import java.util.List;
import org.proyecto.fastdeliveryp_v1.dto.RepartidorDto;
import org.proyecto.fastdeliveryp_v1.service.RepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/repartidors")
public class RepartidorRestController {
  @Autowired
  private RepartidorService service;

  @GetMapping
  public List<RepartidorDto> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public RepartidorDto findById(String id) {
    return service.findById(id);
  }

  @PostMapping
  public RepartidorDto save(RepartidorDto dto) {
    return service.save(dto);
  }

  @DeleteMapping("/{id}")
  public void deleteById(String id) {
    service.deleteById(id);
  }
}
