package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.dto.PedidoProveedorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Controller
public class PedidoProveedorController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String PEDIDO_PROVEEDOR_API_URL = "http://localhost:8080/api/pedidoproveedores";

    @GetMapping("/pedidoproveedores")
    public String getPedidosProveedor(Model model) {
        PedidoProveedorDto[] pedidosArray = restTemplate.getForObject(PEDIDO_PROVEEDOR_API_URL, PedidoProveedorDto[].class);
        List<PedidoProveedorDto> pedidos = Arrays.asList(pedidosArray);

        model.addAttribute("pedidos", pedidos);
        return "proveedor/pedidos-proveedor :: pedidos-proveedor";
    }
}
