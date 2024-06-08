package org.proyecto.fastdeliveryp_v1.repository;

import java.lang.Long;
import org.proyecto.fastdeliveryp_v1.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
}
