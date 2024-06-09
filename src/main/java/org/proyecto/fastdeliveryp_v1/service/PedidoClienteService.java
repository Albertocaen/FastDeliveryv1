package org.proyecto.fastdeliveryp_v1.service;

import org.proyecto.fastdeliveryp_v1.dto.PedidoClienteDto;
import org.proyecto.fastdeliveryp_v1.dto.PersonaDto;
import org.proyecto.fastdeliveryp_v1.entity.*;
import org.proyecto.fastdeliveryp_v1.mapper.PedidoClienteMapper;
import org.proyecto.fastdeliveryp_v1.repository.PedidoClienteProductoRepository;
import org.proyecto.fastdeliveryp_v1.repository.PedidoClienteRepository;
import org.proyecto.fastdeliveryp_v1.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoClienteService {
    @Autowired
    private PedidoClienteRepository pedidoClienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoClienteProductoRepository pedidoClienteProductoRepository;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
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

    @Transactional
    public PedidoCliente savePedido(PedidoCliente pedido, List<PedidoClienteProducto> productos) {
        PedidoCliente savedPedido = pedidoClienteRepository.save(pedido);
        for (PedidoClienteProducto producto : productos) {
            producto.setPedidoCliente(savedPedido);
            pedidoClienteProductoRepository.save(producto);
        }
        return savedPedido;
    }

    public void deletePedido(Integer id) {
        pedidoClienteRepository.deleteById(id);
    }

    public void updatePedidoEstado(Integer id, String estado) {
        PedidoCliente pedido = pedidoClienteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid pedido Id:" + id));
        pedido.setEstado(estado);
        pedidoClienteRepository.save(pedido);

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


    public PedidoClienteDto save(PedidoClienteDto dto) {
        PedidoCliente pedidoCliente = mapper.toEntity(dto);
        PedidoCliente savedPedidoCliente = pedidoClienteRepository.save(pedidoCliente);
        return mapper.toDto(savedPedidoCliente);
    }

    public void deleteById(Integer id) {
        pedidoClienteRepository.deleteById(id);
    }


}
