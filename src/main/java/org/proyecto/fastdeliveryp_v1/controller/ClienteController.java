package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.entity.Cliente;
import org.proyecto.fastdeliveryp_v1.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CRUD de clientes (panel administrativo).
 *
 * <p>Todas las operaciones requieren rol {@code ROLE_ADMIN}, aplicado a nivel de
 * clase con {@link PreAuthorize}. Eso garantiza que aunque {@code SecurityConfig}
 * marque rutas como {@code authenticated()}, sólo un admin pueda ejecutar acciones
 * destructivas.</p>
 *
 * <p><b>Auditoría 2026-04 — cambios aplicados:</b></p>
 * <ul>
 *   <li>Añadido {@code @PreAuthorize("hasAuthority('ROLE_ADMIN')")} a nivel clase.
 *       Antes cualquier usuario autenticado podía listar/editar/eliminar clientes
 *       (broken access control).</li>
 *   <li>{@code deleteCliente} pasa de {@code @GetMapping} a {@code @PostMapping}.
 *       Antes un simple {@code <img src="/clientes/delete/123">} en cualquier email
 *       o web disparaba el delete (CSRF GET-based). HTTP semántico correcto.</li>
 *   <li>El {@code @ModelAttribute} sigue rellenando {@link Cliente} directamente
 *       (mass assignment potencial). El TODO documenta migrar a DTO con {@code @Valid}
 *       cuando el ClienteDto tenga los campos del form.</li>
 * </ul>
 */
@Controller
@RequestMapping("/clientes")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Lista todos los clientes (vista de admin).
     *
     * @param model modelo Spring MVC.
     * @return vista {@code clientes/list}.
     */
    @GetMapping
    public String listClientes(Model model) {
        model.addAttribute("clientes", clienteService.getAllClientes());
        return "clientes/list";
    }

    /**
     * Formulario de creación de cliente (vacío).
     *
     * @param model modelo Spring MVC.
     * @return vista {@code clientes/new}.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/new";
    }

    /**
     * Persiste el cliente enviado por el formulario.
     *
     * <p><b>TODO (deuda técnica):</b> migrar a un {@code ClienteCreateDto} con
     * sólo los campos editables y validar con {@code @Valid}. Hoy
     * {@code @ModelAttribute Cliente} expone toda la entidad — un atacante con
     * conocimiento del modelo puede setear campos no expuestos en el form.
     * Como el endpoint está restringido a ROLE_ADMIN el riesgo es contenido,
     * pero sigue siendo mala práctica.</p>
     *
     * @param cliente entidad rellenada por el formulario HTML.
     * @return redirect a la lista.
     */
    @PostMapping
    public String saveCliente(@ModelAttribute("cliente") Cliente cliente) {
        clienteService.saveCliente(cliente);
        return "redirect:/clientes";
    }

    /**
     * Formulario de edición precargado con los datos del cliente.
     *
     * @param dniCliente DNI del cliente a editar.
     * @param model      modelo Spring MVC.
     * @return vista de edición o redirect a la lista si no existe.
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
     * Actualiza el cliente identificado por el DNI con los datos del formulario.
     *
     * <p>Misma deuda técnica que {@link #saveCliente}: usar DTO en futuro.</p>
     *
     * @param dniCliente DNI del cliente a actualizar.
     * @param cliente    entidad con los datos nuevos.
     * @return redirect a la lista.
     */
    @PostMapping("/edit/{dniCliente}")
    public String updateCliente(@PathVariable("dniCliente") String dniCliente,
                                @ModelAttribute("cliente") Cliente cliente) {
        clienteService.updateCliente(dniCliente, cliente);
        return "redirect:/clientes";
    }

    /**
     * Elimina un cliente.
     *
     * <p><b>Importante:</b> este endpoint es {@code @PostMapping} (antes era
     * {@code @GetMapping}). Razón: las operaciones destructivas no deben ir por
     * GET — un GET es asumido como idempotente y seguro por navegadores, prefetchers,
     * crawlers, y cualquier {@code <img>} o {@code <link>} que apunte a esta URL
     * dispararía la eliminación. La vista que llama a este endpoint debe usar
     * un {@code <form method="post">}.</p>
     *
     * @param dniCliente DNI del cliente a eliminar.
     * @return redirect a la lista.
     */
    @PostMapping("/delete/{dniCliente}")
    public String deleteCliente(@PathVariable("dniCliente") String dniCliente) {
        clienteService.deleteCliente(dniCliente);
        return "redirect:/clientes";
    }
}
