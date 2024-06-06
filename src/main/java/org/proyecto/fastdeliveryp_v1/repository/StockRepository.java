package org.proyecto.fastdeliveryp_v1.repository;

import org.proyecto.fastdeliveryp_v1.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface StockRepository extends JpaRepository<Stock, String> {

    List<Stock> findByDniAdminDniAdmin(String dniAdmin);
}
