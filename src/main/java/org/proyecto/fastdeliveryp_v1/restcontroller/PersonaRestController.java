package org.proyecto.fastdeliveryp_v1.restcontroller;

import java.util.List;

import org.proyecto.fastdeliveryp_v1.dto.PersonaDto;
import org.proyecto.fastdeliveryp_v1.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/personas")
public class PersonaRestController {
    @Autowired
    private PersonaService service;

    @GetMapping
    public List<PersonaDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PersonaDto findById(String id) {
        return service.findById(id);
    }

    @PostMapping
    public PersonaDto save(PersonaDto dto) {
        return service.save(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(String id) {
        service.deleteById(id);
    }
}
