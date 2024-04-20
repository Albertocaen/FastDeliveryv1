package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;

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

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Float getCapacidadCarga() {
        return capacidadCarga;
    }

    public void setCapacidadCarga(Float capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }

    public String getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(String tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

}