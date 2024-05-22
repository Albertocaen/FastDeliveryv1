package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.entity.PedidoCliente;
import org.proyecto.fastdeliveryp_v1.service.PedidoClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoClienteController {
    @Autowired
    private PedidoClienteService pedidoClienteService;

    @GetMapping
    public String listPedidos(Model model) {
        List<PedidoCliente> pedidos = pedidoClienteService.getAllPedidos();
        model.addAttribute("pedidos", pedidos);
        return "pedidos/list";
    }

    @GetMapping("/new")
    public String newPedidoForm(Model model) {
        model.addAttribute("pedido", new PedidoCliente());
        return "pedidos/new";
    }

    @PostMapping
    public String savePedido(@ModelAttribute PedidoCliente pedido) {
        pedidoClienteService.savePedido(pedido);
        return "redirect:/pedidos";
    }

    @GetMapping("/edit/{id}")
    public String editPedidoForm(@PathVariable Integer id, Model model) {
        PedidoCliente pedido = pedidoClienteService.getPedidoById(id);
        model.addAttribute("pedido", pedido);
        return "pedidos/edit";
    }

    @PostMapping("/update")
    public String updatePedido(@ModelAttribute PedidoCliente pedido) {
        pedidoClienteService.savePedido(pedido);
        return "redirect:/pedidos";
    }

    @GetMapping("/delete/{id}")
    public String deletePedido(@PathVariable Integer id) {
        pedidoClienteService.deletePedido(id);
        return "redirect:/pedidos";
    }
}
