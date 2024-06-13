package org.proyecto.fastdeliveryp_v1.repository;

import org.proyecto.fastdeliveryp_v1.entity.Cliente;
import org.proyecto.fastdeliveryp_v1.entity.PedidoClienteProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoClienteProductoRepository extends JpaRepository<PedidoClienteProducto, Integer> {
    List<PedidoClienteProducto> findByPedidoClienteDniClientePedido(Cliente cliente);
}
