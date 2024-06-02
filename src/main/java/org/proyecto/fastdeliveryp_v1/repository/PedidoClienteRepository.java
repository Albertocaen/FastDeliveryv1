package org.proyecto.fastdeliveryp_v1.repository;

import org.proyecto.fastdeliveryp_v1.entity.Cliente;
import org.proyecto.fastdeliveryp_v1.entity.PedidoCliente;
import org.proyecto.fastdeliveryp_v1.entity.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoClienteRepository extends JpaRepository<PedidoCliente, Integer> {
    List<PedidoCliente> findByDniRepartidorPedido(Repartidor dniRepartidorPedido);
    List<PedidoCliente> findByDniClientePedido(Cliente cliente);
}
