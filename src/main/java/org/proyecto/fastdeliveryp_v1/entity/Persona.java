package org.proyecto.fastdeliveryp_v1.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Entidad que representa una persona en la base de datos.
 */
@Builder
@Entity
@Table(name = "PERSONA")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Persona {
    /**
     * DNI de la persona, que actúa como clave primaria.
     */
    @Id
    @EqualsAndHashCode.Include
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

    /**
     * Relación uno a uno con la entidad Cliente.
     * Representa el cliente asociado a la persona.
     */
    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Cliente cliente;

    /**
     * Relación uno a uno con la entidad Repartidor.
     * Representa el repartidor asociado a la persona.
     */
    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Repartidor repartidor;

    /**
     * Relación uno a uno con la entidad Admin.
     * Representa el administrador asociado a la persona.
     */
    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Admin admin;

    /**
     * Obtiene el rol de la persona basado en su nombre de usuario.
     * @return El rol de la persona (ROLE_ADMIN o ROLE_USER).
     */
    public String getRole() {

        if ("admin".equalsIgnoreCase(this.usuario)) {
            return "ROLE_ADMIN";
        } else {
            return "ROLE_USER";
        }
    }
}