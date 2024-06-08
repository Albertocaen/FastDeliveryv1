package org.proyecto.fastdeliveryp_v1.repository;

import java.lang.Long;
import org.proyecto.fastdeliveryp_v1.entity.Coche;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CocheRepository extends JpaRepository<Coche, Long> {
}
