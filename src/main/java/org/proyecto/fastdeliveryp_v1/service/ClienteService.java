package org.proyecto.fastdeliveryp_v1.service;

import org.proyecto.fastdeliveryp_v1.entity.*;
import org.proyecto.fastdeliveryp_v1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private RepartidorRepository repartidorRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PedidoClienteRepository pedidoClienteRepository;


    @Autowired
    private PedidoProveedorRepository pedidoProveedorRepository;


    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public Cliente getClienteById(String dni) {
        return clienteRepository.findById(dni).orElse(null);
    }

    public Cliente saveCliente(Cliente cliente) {
        // Asignar el DNI de Cliente a la Persona
        Persona persona = cliente.getPersona();
        persona.setDni(cliente.getDniCliente());

        // Guardar la persona antes del cliente
        personaRepository.save(persona);

        // Luego guardar el cliente
        return clienteRepository.save(cliente);
    }

    public void updateCliente(String dniCliente, Cliente cliente) {
        // Asegúrate de que el DNI de la Persona está asignado
        cliente.getPersona().setDni(cliente.getDniCliente());

        // Asegúrate de que el campo usuario tiene un valor permitido
        if (!cliente.getPersona().getUsuario().equals("admin") &&
                !cliente.getPersona().getUsuario().equals("cliente") &&
                !cliente.getPersona().getUsuario().equals("repartidor")) {
            cliente.getPersona().setUsuario("cliente"); // Asigna un valor por defecto si no es válido
        }

        // Guardar la persona antes del cliente
        personaRepository.save(cliente.getPersona());

        // Luego guardar el cliente
        clienteRepository.save(cliente);
    }
    @Transactional
    public void deleteCliente(String dniCliente) {
        Cliente cliente = clienteRepository.findById(dniCliente).orElse(null);
        if (cliente != null) {
            Persona persona = cliente.getPersona();

            // Verificar y eliminar cualquier relación con Repartidor y Admin si existen
            if (persona != null) {
                Repartidor repartidor = repartidorRepository.findByPersonaDni(persona.getDni());
                if (repartidor != null) {
                    // Encontrar y eliminar pedidos relacionados con el repartidor
                    List<PedidoCliente> pedidos = pedidoClienteRepository.findByDniRepartidorPedido(repartidor);
                    for (PedidoCliente pedido : pedidos) {

                    }
                    // Eliminar pedidos del repartidor
                    pedidoClienteRepository.deleteAll(pedidos);

                    repartidorRepository.delete(repartidor);
                }

                Admin admin = adminRepository.findByPersonaDni(persona.getDni());
                if (admin != null) {
                    // Eliminar stocks relacionados con el admin
                    List<Stock> stocks = stockRepository.findByDniAdmin(admin);
                    for (Stock stock : stocks) {
                        stockRepository.delete(stock);
                    }

                    // Encontrar y eliminar pedidos proveedores relacionados con el admin
                    List<PedidoProveedor> pedidosProveedor = pedidoProveedorRepository.findByDniAdminPedido_DniAdmin(admin);
                    for (PedidoProveedor pedidoProveedor : pedidosProveedor) {

                    }
                    // Eliminar pedidos del proveedor
                    pedidoProveedorRepository.deleteAll(pedidosProveedor);

                    adminRepository.delete(admin);
                }

                // Finalmente, eliminar la persona
                clienteRepository.deletePersonaByDni(persona.getDni());
            }

            // Limpiar las relaciones antes de eliminar el cliente
            clienteRepository.delete(cliente);
        }
    }
}


