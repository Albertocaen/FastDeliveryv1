package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "COCHE")
@PrimaryKeyJoinColumn(name = "placa_vehiculo")
public class Coche extends Vehiculo {

    @Column(name = "capacidad_carga")
    private Float capacidadCarga;

    @Column(name = "tipo_combustible", length = 50)
    private String tipoCombustible;

}