package org.proyecto.fastdeliveryp_v1.service;

import org.proyecto.fastdeliveryp_v1.entity.PedidoCliente;
import org.proyecto.fastdeliveryp_v1.entity.Persona;
import org.proyecto.fastdeliveryp_v1.repository.PedidoClienteRepository;
import org.proyecto.fastdeliveryp_v1.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoClienteService {
    @Autowired
    private PedidoClienteRepository pedidoClienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    public List<PedidoCliente> getAllPedidos() {
        return pedidoClienteRepository.findAll();
    }

    public PedidoCliente getPedidoById(Integer id) {
        return pedidoClienteRepository.findById(id).orElse(null);
    }

    public PedidoCliente savePedido(PedidoCliente pedido) {
        return pedidoClienteRepository.save(pedido);
    }

    public void deletePedido(Integer id) {
        pedidoClienteRepository.deleteById(id);
    }

    @Transactional
    public void deletePersona(String dni) {
        Persona persona = personaRepository.findById(dni).orElse(null);
        if (persona != null) {
            personaRepository.delete(persona);
        }
    }
}
