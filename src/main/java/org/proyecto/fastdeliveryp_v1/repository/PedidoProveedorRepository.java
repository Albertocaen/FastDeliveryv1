package org.proyecto.fastdeliveryp_v1.repository;

import org.proyecto.fastdeliveryp_v1.entity.Admin;
import org.proyecto.fastdeliveryp_v1.entity.PedidoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoProveedorRepository extends JpaRepository<PedidoProveedor, Integer> {
    List<PedidoProveedor> findByDniAdminPedido_DniAdmin(Admin dniAdmin);
}

