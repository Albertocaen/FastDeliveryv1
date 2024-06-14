package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.dto.PedidoProveedorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${api.base.url}")
    private String apiBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Maneja las solicitudes GET para obtener todos los pedidos de proveedores.
     * @param model El modelo para pasar datos a la vista.
     * @return la vista de los pedidos de proveedores.
     */
    @GetMapping
    public String getPedidosProveedor(Model model) {
        String PEDIDO_PROVEEDOR_API_URL = apiBaseUrl +"/api/pedidoproveedores";
        PedidoProveedorDto[] pedidosArray = restTemplate.getForObject(PEDIDO_PROVEEDOR_API_URL, PedidoProveedorDto[].class);
        List<PedidoProveedorDto> pedidos = Arrays.asList(pedidosArray);

        model.addAttribute("pedidos", pedidos);
        return "proveedor/pedidos-proveedor :: pedidos-proveedor";
    }

    /**
     * Maneja las solicitudes GET para obtener un pedido de proveedor específico por su ID.
     * @param id El ID del pedido de proveedor.
     * @param model El modelo para pasar datos a la vista.
     * @return la vista del detalle del pedido de proveedor.
     */
    @GetMapping("/{id}")
    public String getPedidoProveedor(@PathVariable Integer id, Model model) {
        String PEDIDO_PROVEEDOR_API_URL = apiBaseUrl +"/api/pedidoproveedores";
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


