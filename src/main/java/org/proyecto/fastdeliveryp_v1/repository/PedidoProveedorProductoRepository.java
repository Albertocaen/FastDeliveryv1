package org.proyecto.fastdeliveryp_v1.repository;

import org.proyecto.fastdeliveryp_v1.entity.PedidoProveedorProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoProveedorProductoRepository extends JpaRepository<PedidoProveedorProducto, Integer> {
    List<PedidoProveedorProducto> findByPedidoProveedorId(Integer pedidoProveedorId);
    void deleteByPedidoProveedorId(Integer pedidoProveedorId);
}
