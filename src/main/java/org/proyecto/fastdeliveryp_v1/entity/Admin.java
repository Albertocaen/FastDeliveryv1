package org.proyecto.fastdeliveryp_v1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


import java.util.List;


@Data
@Entity
@Getter
@Setter
@Table(name = "ADMIN")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Admin {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "DNI_ADMIN", nullable = false, length = 20)
    private String dniAdmin;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "DNI_ADMIN", nullable = false)
    @JsonBackReference
    private Persona persona;


    @OneToMany(mappedBy = "dniAdmin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stock> stocks;


    @OneToMany(mappedBy = "dniAdminPedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoProveedor> pedidosProveedor;


}