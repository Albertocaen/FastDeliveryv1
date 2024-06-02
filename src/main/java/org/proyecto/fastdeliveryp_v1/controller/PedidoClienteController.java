package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.entity.Cliente;
import org.proyecto.fastdeliveryp_v1.entity.PedidoCliente;
import org.proyecto.fastdeliveryp_v1.entity.Repartidor;
import org.proyecto.fastdeliveryp_v1.service.ClienteService;
import org.proyecto.fastdeliveryp_v1.service.PedidoClienteService;
import org.proyecto.fastdeliveryp_v1.service.ProductoService;
import org.proyecto.fastdeliveryp_v1.service.RepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;


@Controller
@RequestMapping("/pedidos")
public class PedidoClienteController {

    @Autowired
    private PedidoClienteService pedidoClienteService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private RepartidorService repartidorService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public String listPedidos(Model model, Principal principal) {
        List<PedidoCliente> pedidos;
        String username = principal.getName();
        Cliente cliente = clienteService.getClienteByEmail(username);

        if (hasRole("ROLE_ADMIN")) {
            pedidos = pedidoClienteService.getAllPedidos();
        } else {
            pedidos = pedidoClienteService.getPedidosByCliente(cliente);
        }

        model.addAttribute("pedidos", pedidos);
        return "pedidos/list";
    }

    @GetMapping("/new")
    public String newPedidoForm(Model model, Principal principal) {
        model.addAttribute("pedido", new PedidoCliente());
        model.addAttribute("productos", productoService.getAllProductos());

        if (hasRole("ROLE_ADMIN")) {
            model.addAttribute("clientes", clienteService.getAllClientes());
        }

        return "pedidos/new";
    }

    @PostMapping
    public String savePedido(@ModelAttribute PedidoCliente pedido, Principal principal) {
        Repartidor repartidorAsignado = repartidorService.asignarRepartidorDisponible();

        pedido.setDniRepartidorPedido(repartidorAsignado);
        pedido.setEstado("En Curso");

        if (hasRole("ROLE_ADMIN")) {
            // El cliente se asigna a través del formulario
        } else {
            String username = principal.getName();
            Cliente cliente = clienteService.getClienteByEmail(username);
            pedido.setDniClientePedido(cliente);
        }

        pedidoClienteService.savePedido(pedido);
        return "redirect:/pedidos";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/estado/{id}")
    public String changeEstadoForm(@PathVariable Integer id, Model model) {
        PedidoCliente pedido = pedidoClienteService.getPedidoById(id);
        model.addAttribute("pedido", pedido);
        return "pedidos/estado";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/estado")
    public String changeEstado(@ModelAttribute PedidoCliente pedido) {
        pedidoClienteService.updatePedidoEstado(pedido.getId(), pedido.getEstado());
        messagingTemplate.convertAndSend("/topic/notifications", "El estado del pedido con ID " + pedido.getId() + " ha sido actualizado a " + pedido.getEstado());
        return "redirect:/pedidos";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String deletePedido(@PathVariable Integer id) {
        pedidoClienteService.deletePedido(id);
        return "redirect:/pedidos";
    }

    // Método para verificar si el usuario tiene un rol específico
    private boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }
}