package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.dto.StockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class StockController {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.base.url}")
    private String apiBaseUrl;


    /**
     * Maneja las solicitudes GET para obtener todos los stocks.
     *
     * @param model El modelo para pasar datos a la vista.
     * @return la vista de la lista de stocks.
     */
    @GetMapping("/stocks")
    public String getStocks(Model model) {
        String STOCKS_API_URL = apiBaseUrl + "/api/stocks";
        StockDto[] stocksArray = restTemplate.getForObject(STOCKS_API_URL, StockDto[].class);
        List<StockDto> stocks = Arrays.asList(stocksArray);

        model.addAttribute("stocks", stocks);
        return "stock/stocks :: stocks";
    }
}
