package org.proyecto.fastdeliveryp_v1.service;

import org.proyecto.fastdeliveryp_v1.entity.Cliente;
import org.proyecto.fastdeliveryp_v1.entity.Notification;
import org.proyecto.fastdeliveryp_v1.entity.NotificationMessage;
import org.proyecto.fastdeliveryp_v1.entity.PedidoCliente;
import org.proyecto.fastdeliveryp_v1.repository.PedidoClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PedidoClienteService {
    @Autowired
    private PedidoClienteRepository pedidoClienteRepository;

    @Autowired
    private SimpMessagingTemplate template;


    public List<PedidoCliente> getAllPedidos() {
        return pedidoClienteRepository.findAll();
    }

    public PedidoCliente getPedidoById(Integer id) {
        return pedidoClienteRepository.findById(id).orElse(null);
    }


    public List<PedidoCliente> getPedidosByCliente(Cliente cliente) {
        return pedidoClienteRepository.findByDniClientePedido(cliente);
    }

    public PedidoCliente savePedido(PedidoCliente pedido) {
        return pedidoClienteRepository.save(pedido);
    }

    public void deletePedido(Integer id) {
        pedidoClienteRepository.deleteById(id);
    }
    public void updatePedidoEstado(Integer id, String estado) {
        PedidoCliente pedido = pedidoClienteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid pedido Id:" + id));
        pedido.setEstado(estado);
        pedidoClienteRepository.save(pedido);

        // Enviar notificación
        NotificationMessage message = new NotificationMessage();
        message.setPedidoId(id);
        template.convertAndSend("/topic/notifications", new Notification("El estado de tu pedido ha cambiado a " + estado));
    }

}
