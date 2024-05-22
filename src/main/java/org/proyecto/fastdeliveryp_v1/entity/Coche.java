package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "COCHE")
public class Coche {
    @Id
    @Column(name = "placa_vehiculo", nullable = false, length = 20)
    private String placaVehiculo;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "placa_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "capacidad_carga")
    private Float capacidadCarga;

    @Column(name = "tipo_combustible", length = 50)
    private String tipoCombustible;


}