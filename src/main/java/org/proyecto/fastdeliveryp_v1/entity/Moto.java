package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entidad que representa una moto en la base de datos.
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "MOTO")
@PrimaryKeyJoinColumn(name = "placa_vehiculo")
public class Moto extends Vehiculo {


    @Column(name = "cilindrada")
    private Integer cilindrada;

}