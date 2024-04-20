package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "INCIDENCIA", indexes = {
        @Index(name = "id_stock", columnList = "id_stock")
})
public class Incidencia {
    @Id
    @Column(name = "id_incidencia", nullable = false)
    private Integer id;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "hora")
    private LocalTime hora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_stock")
    private Stock idStock;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Stock getIdStock() {
        return idStock;
    }

    public void setIdStock(Stock idStock) {
        this.idStock = idStock;
    }

}