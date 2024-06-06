package org.proyecto.fastdeliveryp_v1.repository;

import org.proyecto.fastdeliveryp_v1.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {
    @Query("SELECT c FROM Cliente c WHERE c.dniCliente = :dniCliente")
    Cliente findByDniCliente(@Param("dniCliente") String dniCliente);

    @Query("SELECT c FROM Cliente c WHERE c.persona.email = :email")
    Cliente findByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM Persona p WHERE p.dni = ?1")
    void deletePersonaByDni(String dni);
}