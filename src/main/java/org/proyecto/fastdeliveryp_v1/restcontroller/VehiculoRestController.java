package org.proyecto.fastdeliveryp_v1.restcontroller;

import java.lang.Long;
import java.util.List;

import org.proyecto.fastdeliveryp_v1.dto.VehiculoDto;
import org.proyecto.fastdeliveryp_v1.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoRestController {
    @Autowired
    private VehiculoService service;

    @GetMapping
    public List<VehiculoDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public VehiculoDto findById(Long id) {
        return service.findById(id);
    }

    @PostMapping
    public VehiculoDto save(VehiculoDto dto) {
        return service.save(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(Long id) {
        service.deleteById(id);
    }
}
