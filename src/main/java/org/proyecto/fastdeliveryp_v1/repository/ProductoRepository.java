package org.proyecto.fastdeliveryp_v1.repository;

import org.proyecto.fastdeliveryp_v1.entity.Producto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    @Query("SELECT p FROM Producto p ORDER BY p.precio DESC")
    List<Producto> findTopProductos(Pageable pageable);
}
