package org.proyecto.fastdeliveryp_v1.controller;


import org.proyecto.fastdeliveryp_v1.dto.ProveedorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${api.base.url}")
    private String apiBaseUrl;

    /**
     * Maneja las solicitudes GET para obtener todos los proveedores.
     *
     * @param model El modelo para pasar datos a la vista.
     * @return la vista de la lista de proveedores.
     */
    @GetMapping("/proveedores")
    public String getProveedores(Model model) {
        String PROVEEDORES_API_URL = apiBaseUrl + "/api/proveedores";
        ProveedorDto[] proveedoresArray = restTemplate.getForObject(PROVEEDORES_API_URL, ProveedorDto[].class);
        List<ProveedorDto> proveedores = Arrays.asList(proveedoresArray);

        model.addAttribute("proveedores", proveedores);
        return "proveedor/proveedor :: proveedor";
    }
}
