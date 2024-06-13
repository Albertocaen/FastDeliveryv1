package org.proyecto.fastdeliveryp_v1.restcontroller;

import java.lang.Long;
import java.util.List;

import org.proyecto.fastdeliveryp_v1.dto.MotoDto;
import org.proyecto.fastdeliveryp_v1.service.MotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/motos")
public class MotoRestController {
    @Autowired
    private MotoService service;

    @GetMapping
    public List<MotoDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public MotoDto findById(Long id) {
        return service.findById(id);
    }


    @PostMapping("/save")
    public ResponseEntity<MotoDto> save(@RequestBody MotoDto motoDto) {
        try {
            MotoDto savedMoto = service.save(motoDto);
            return ResponseEntity.ok(savedMoto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteById(Long id) {
        service.deleteById(id);
    }
}
