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

    /**
     * Muestra la lista de todos los clientes.
     *
     * @param model El modelo para pasar datos a la vista.
     * @return la vista de la lista de clientes.
     */
    @GetMapping
    public String listClientes(Model model) {
        model.addAttribute("clientes", clienteService.getAllClientes());
        return "clientes/list";
    }

    /**
     * Muestra el formulario para crear un nuevo cliente.
     *
     * @param model El modelo para pasar datos a la vista.
     * @return la vista del formulario de nuevo cliente.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/new";
    }

    /**
     * Guarda un nuevo cliente.
     *
     * @param cliente El cliente a guardar.
     * @return redirección a la vista de la lista de clientes.
     */
    @PostMapping
    public String saveCliente(@ModelAttribute("cliente") Cliente cliente) {
        clienteService.saveCliente(cliente);
        return "redirect:/clientes";
    }

    /**
     * Muestra el formulario para editar un cliente existente.
     *
     * @param dniCliente El DNI del cliente a editar.
     * @param model      El modelo para pasar datos a la vista.
     * @return la vista del formulario de edición de cliente.
     */
    @GetMapping("/edit/{dniCliente}")
    public String showEditForm(@PathVariable("dniCliente") String dniCliente, Model model) {
        Cliente cliente = clienteService.getClienteById(dniCliente);
        if (cliente == null) {
            return "redirect:/clientes";
        }
        model.addAttribute("cliente", cliente);
        return "clientes/edit";
    }

    /**
     * Actualiza un cliente existente.
     *
     * @param dniCliente El DNI del cliente a actualizar.
     * @param cliente    Los datos actualizados del cliente.
     * @return redirección a la vista de la lista de clientes.
     */
    @PostMapping("/edit/{dniCliente}")
    public String updateCliente(@PathVariable("dniCliente") String dniCliente, @ModelAttribute("cliente") Cliente cliente) {
        clienteService.updateCliente(dniCliente, cliente);
        return "redirect:/clientes";
    }

    /**
     * Elimina un cliente.
     *
     * @param dniCliente El DNI del cliente a eliminar.
     * @return redirección a la vista de la lista de clientes.
     */
    @GetMapping("/delete/{dniCliente}")
    public String deleteCliente(@PathVariable("dniCliente") String dniCliente) {
        clienteService.deleteCliente(dniCliente);
        return "redirect:/clientes";
    }
}
