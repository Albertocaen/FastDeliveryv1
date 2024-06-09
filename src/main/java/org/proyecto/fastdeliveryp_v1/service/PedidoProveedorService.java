package org.proyecto.fastdeliveryp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.proyecto.fastdeliveryp_v1.dto.PedidoProveedorDto;

import org.proyecto.fastdeliveryp_v1.dto.PedidoProveedorProductoInfo;
import org.proyecto.fastdeliveryp_v1.entity.PedidoProveedor;
import org.proyecto.fastdeliveryp_v1.entity.PedidoProveedorProducto;
import org.proyecto.fastdeliveryp_v1.mapper.PedidoProveedorMapper;
import org.proyecto.fastdeliveryp_v1.mapper.PedidoProveedorProductoMapper;
import org.proyecto.fastdeliveryp_v1.repository.PedidoProveedorProductoRepository;
import org.proyecto.fastdeliveryp_v1.repository.PedidoProveedorRepository;
import org.proyecto.fastdeliveryp_v1.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoProveedorService {

    @Autowired
    private PedidoProveedorRepository pedidoProveedorRepository;

    @Autowired
    private PedidoProveedorProductoRepository pedidoProveedorProductoRepository;

    @Autowired
    private PedidoProveedorMapper pedidoProveedorMapper;

    @Autowired
    private PedidoProveedorProductoMapper pedidoProveedorProductoMapper;

    public List<PedidoProveedorDto> findAll() {
        List<PedidoProveedor> pedidos = pedidoProveedorRepository.findAll();
        return pedidos.stream()
                .map(this::convertToDtoWithProductos)
                .collect(Collectors.toList());
    }

    public PedidoProveedorDto findById(Integer id) {
        PedidoProveedor pedido = pedidoProveedorRepository.findById(id).orElse(null);
        return convertToDtoWithProductos(pedido);
    }

    public PedidoProveedorDto save(PedidoProveedorDto dto) {
        PedidoProveedor pedido = pedidoProveedorMapper.toEntity(dto);
        PedidoProveedor savedPedido = pedidoProveedorRepository.save(pedido);
        return pedidoProveedorMapper.toDto(savedPedido);
    }

    public void saveProductos(List<PedidoProveedorProductoInfo> productos, Integer pedidoId) {
        PedidoProveedor pedido = pedidoProveedorRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pedido Id: " + pedidoId));
        List<PedidoProveedorProducto> entities = productos.stream()
                .map(pedidoProveedorProductoMapper::toEntity)
                .peek(p -> p.setPedidoProveedor(pedido))
                .collect(Collectors.toList());
        pedidoProveedorProductoRepository.saveAll(entities);
    }

    public void deleteById(Integer id) {
        pedidoProveedorRepository.deleteById(id);
    }

    private PedidoProveedorDto convertToDtoWithProductos(PedidoProveedor pedido) {
        PedidoProveedorDto dto = pedidoProveedorMapper.toDto(pedido);
        List<PedidoProveedorProducto> productos = pedidoProveedorProductoRepository.findByPedidoProveedorId(pedido.getId());
        List<PedidoProveedorProductoInfo> productosDto = productos.stream()
                .map(pedidoProveedorProductoMapper::toInfo)
                .collect(Collectors.toList());
        dto.setProductos(productosDto);
        return dto;
    }
}