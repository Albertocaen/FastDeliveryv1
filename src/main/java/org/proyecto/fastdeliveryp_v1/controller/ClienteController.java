package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.entity.Cliente;
import org.proyecto.fastdeliveryp_v1.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listClientes(Model model) {
        model.addAttribute("clientes", clienteService.getAllClientes());
        return "clientes/list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/new";
    }

    @PostMapping
    public String saveCliente(@ModelAttribute("cliente") Cliente cliente) {
        clienteService.saveCliente(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/edit/{dniCliente}")
    public String showEditForm(@PathVariable("dniCliente") String dniCliente, Model model) {
        Cliente cliente = clienteService.getClienteById(dniCliente);
        if (cliente == null) {
            return "redirect:/clientes";
        }
        model.addAttribute("cliente", cliente);
        return "clientes/edit";
    }

    @PostMapping("/edit/{dniCliente}")
    public String updateCliente(@PathVariable("dniCliente") String dniCliente, @ModelAttribute("cliente") Cliente cliente) {
        clienteService.updateCliente(dniCliente, cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/delete/{dniCliente}")
    public String deleteCliente(@PathVariable("dniCliente") String dniCliente) {
        clienteService.deleteCliente(dniCliente);
        return "redirect:/clientes";
    }
}
