package org.proyecto.fastdeliveryp_v1.repository;

import java.lang.Long;
import org.proyecto.fastdeliveryp_v1.entity.Moto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotoRepository extends JpaRepository<Moto, Long> {
}
