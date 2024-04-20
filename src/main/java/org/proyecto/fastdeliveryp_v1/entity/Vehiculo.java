package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "VEHICULO")
public class Vehiculo {
    @Id
    @Column(name = "placa_vehiculo", nullable = false, length = 20)
    private String placaVehiculo;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "modelo", length = 50)
    private String modelo;

    @Column(name = "color", length = 50)
    private String color;

    @Lob
    @Column(name = "tipo")
    private String tipo;

    @OneToOne(mappedBy = "placaVehiculo")
    private Coche coche;

    @OneToOne(mappedBy = "placaVehiculo")
    private Moto moto;

    @OneToMany(mappedBy = "placaVehiculo")
    private Set<Repartidor> repartidors = new LinkedHashSet<>();

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Coche getCoche() {
        return coche;
    }

    public void setCoche(Coche coche) {
        this.coche = coche;
    }

    public Moto getMoto() {
        return moto;
    }

    public void setMoto(Moto moto) {
        this.moto = moto;
    }

    public Set<Repartidor> getRepartidors() {
        return repartidors;
    }

    public void setRepartidors(Set<Repartidor> repartidors) {
        this.repartidors = repartidors;
    }

}