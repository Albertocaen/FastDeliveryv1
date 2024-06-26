package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.dto.MotoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class MotoController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Maneja las solicitudes GET para obtener la lista de motos.
     *
     * @param model El modelo para pasar datos a la vista.
     * @return la vista de la lista de motos.
     */
    @GetMapping("/motos")
    public String getMotos(Model model) {
        String apiUrl = "http://localhost:8080/api/motos";
        MotoDto[] motos = restTemplate.getForObject(apiUrl, MotoDto[].class);

        model.addAttribute("motos", motos);
        return "moto/moto"; //
    }
}
