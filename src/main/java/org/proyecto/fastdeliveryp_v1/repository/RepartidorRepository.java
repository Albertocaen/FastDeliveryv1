package org.proyecto.fastdeliveryp_v1.repository;

import org.proyecto.fastdeliveryp_v1.entity.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepartidorRepository extends JpaRepository<Repartidor, String> {
    List<Repartidor> findByEstadoDeDisponibilidadAndCantidadPedidosLessThanEqual(String estadoDeDisponibilidad, Integer cantidadPedidos);
}
