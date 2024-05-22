package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PERSONA")
@Data
public class Persona {
    @Id
    @Column(name = "DNI", nullable = false, length = 20)
    private String dni;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "apellido", length = 50)
    private String apellido;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "contrasena", length = 100)
    private String contraseña;

    @Column(name = "usuario", length = 50)
    private String usuario;

    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cliente cliente;

    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private Repartidor repartidor;

    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private Admin admin;
}