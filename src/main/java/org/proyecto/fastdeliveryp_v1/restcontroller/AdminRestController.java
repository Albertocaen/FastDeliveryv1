package org.proyecto.fastdeliveryp_v1.restcontroller;

import java.util.List;
import org.proyecto.fastdeliveryp_v1.dto.AdminDto;
import org.proyecto.fastdeliveryp_v1.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins")
public class AdminRestController {
  @Autowired
  private AdminService service;

  @GetMapping
  public List<AdminDto> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public AdminDto findById(String id) {
    return service.findById(id);
  }

  @PostMapping
  public AdminDto save(AdminDto dto) {
    return service.save(dto);
  }

  @DeleteMapping("/{id}")
  public void deleteById(String id) {
    service.deleteById(id);
  }
}
