package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.dto.RepartidorDto;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String REPARTIDORES_API_URL = "http://localhost:8080/api/repartidores";

    @GetMapping("/repartidores")
    public String getRepartidores(Model model) {
        RepartidorDto[] repartidoresArray = restTemplate.getForObject(REPARTIDORES_API_URL, RepartidorDto[].class);
        List<RepartidorDto> repartidores = Arrays.asList(repartidoresArray);

        model.addAttribute("repartidores", repartidores);
        return "repartidores/repartidores :: repartidores";
    }
}
