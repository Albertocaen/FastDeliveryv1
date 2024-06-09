package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.dto.PedidoProveedorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/pedidoproveedores")
public class PedidoProveedorController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String PEDIDO_PROVEEDOR_API_URL = "http://localhost:8080/api/pedidoproveedores";

    @GetMapping
    public String getPedidosProveedor(Model model) {
        PedidoProveedorDto[] pedidosArray = restTemplate.getForObject(PEDIDO_PROVEEDOR_API_URL, PedidoProveedorDto[].class);
        List<PedidoProveedorDto> pedidos = Arrays.asList(pedidosArray);

        model.addAttribute("pedidos", pedidos);
        return "proveedor/pedidos-proveedor :: pedidos-proveedor";
    }

    @GetMapping("/{id}")
    public String getPedidoProveedor(@PathVariable Integer id, Model model) {
        PedidoProveedorDto pedido = restTemplate.getForObject(PEDIDO_PROVEEDOR_API_URL + "/" + id, PedidoProveedorDto.class);

        if (pedido != null) {
            model.addAttribute("pedido", pedido);
            model.addAttribute("productos", pedido.getProductos());
        } else {
            model.addAttribute("pedido", new PedidoProveedorDto());
            model.addAttribute("productos", Collections.emptyList());
        }
        return "proveedor/pedido-detalle";
    }
}


