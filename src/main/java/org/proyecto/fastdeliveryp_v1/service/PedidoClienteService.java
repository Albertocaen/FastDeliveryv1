package org.proyecto.fastdeliveryp_v1.service;

import org.proyecto.fastdeliveryp_v1.dto.PedidoClienteDto;
import org.proyecto.fastdeliveryp_v1.dto.PersonaDto;
import org.proyecto.fastdeliveryp_v1.entity.*;
import org.proyecto.fastdeliveryp_v1.mapper.PedidoClienteMapper;
import org.proyecto.fastdeliveryp_v1.repository.PedidoClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoClienteService {
    @Autowired
    private PedidoClienteRepository pedidoClienteRepository;

    @Autowired
    private SimpMessagingTemplate template;

    PedidoClienteMapper mapper;


    public List<PedidoCliente> getAllPedidos() {
        return pedidoClienteRepository.findAll();
    }

    public PedidoCliente getPedidoById(Integer id) {
        return pedidoClienteRepository.findById(id).orElse(null);
    }

    public List<PedidoCliente> getPedidosByRepartidor(Repartidor repartidor) {
        return pedidoClienteRepository.findByDniRepartidorPedido(repartidor);
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



    public List<PedidoClienteDto> findAll() {
        List<PedidoCliente> pedidos = pedidoClienteRepository.findAll();
        return pedidos.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    public PedidoClienteDto findById(Integer id) {
        return pedidoClienteRepository.findById(id).map(mapper::toDto).orElse(null);
    }

    @PostMapping
    public PedidoClienteDto save(PedidoClienteDto dto) {
        PedidoCliente pedidoCliente = mapper.toEntity(dto);
        PedidoCliente savedPedidoCliente = pedidoClienteRepository.save(pedidoCliente);
        return mapper.toDto(savedPedidoCliente);
    }

    public void deleteById(Integer id) {
        pedidoClienteRepository.deleteById(id);
    }


}
