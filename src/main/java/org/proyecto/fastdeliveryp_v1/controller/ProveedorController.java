package org.proyecto.fastdeliveryp_v1.controller;


import org.proyecto.fastdeliveryp_v1.dto.ProveedorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class ProveedorController {
    @Autowired
    private RestTemplate restTemplate;

    private static final String PROVEEDORES_API_URL = "http://localhost:8080/api/proveedores";

    @GetMapping("/proveedores")
    public String getProveedores(Model model) {
        ProveedorDto[] proveedoresArray = restTemplate.getForObject(PROVEEDORES_API_URL, ProveedorDto[].class);
        List<ProveedorDto> proveedores = Arrays.asList(proveedoresArray);

        model.addAttribute("proveedores", proveedores);
        return "proveedor/proveedor :: proveedor";
    }
}
