package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MOTO")
public class Moto {
    @Id
    @Column(name = "placa_vehiculo", nullable = false, length = 20)
    private String placaVehiculo;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "placa_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "cilindrada")
    private Integer cilindrada;

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

    public Integer getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(Integer cilindrada) {
        this.cilindrada = cilindrada;
    }

}