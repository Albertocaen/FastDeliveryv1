package org.proyecto.fastdeliveryp_v1.repository;

import org.proyecto.fastdeliveryp_v1.entity.Admin;
import org.proyecto.fastdeliveryp_v1.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, String> {
    void deleteByDniAdmin(Admin dniAdmin);
    List<Stock> findByDniAdmin(Admin dniAdmin);

    @Query("SELECT a FROM Admin a WHERE a.dniAdmin = :dniAdmin")
    Admin findAdminByDni(@Param("dniAdmin") String dniAdmin);
}
