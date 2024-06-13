package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.dto.RepartidorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class RepartidorController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    /**
     * Maneja las solicitudes GET para obtener todos los repartidores.
     *
     * @param model El modelo para pasar datos a la vista.
     * @return la vista de la lista de repartidores.
     */
    @GetMapping("/repartidores")
    public String getRepartidores(Model model) {
        String REPARTIDORES_API_URL = apiBaseUrl + "/api/repartidores";
        RepartidorDto[] repartidoresArray = restTemplate.getForObject(REPARTIDORES_API_URL, RepartidorDto[].class);
        List<RepartidorDto> repartidores = Arrays.asList(repartidoresArray);

        model.addAttribute("repartidores", repartidores);
        return "repartidores/repartidores :: repartidores";
    }
}
