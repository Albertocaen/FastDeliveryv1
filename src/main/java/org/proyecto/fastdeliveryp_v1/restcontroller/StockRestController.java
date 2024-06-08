package org.proyecto.fastdeliveryp_v1.restcontroller;

import java.lang.Long;
import java.util.List;
import org.proyecto.fastdeliveryp_v1.dto.StockDto;
import org.proyecto.fastdeliveryp_v1.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stocks")
public class StockRestController {
  @Autowired
  private StockService service;

  @GetMapping
  public List<StockDto> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public StockDto findById(String id) {
    return service.findById(id);
  }

  @PostMapping
  public StockDto save(StockDto dto) {
    return service.save(dto);
  }

  @DeleteMapping("/{id}")
  public void deleteById(String id) {
    service.deleteById(id);
  }
}
