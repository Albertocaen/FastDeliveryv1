package org.proyecto.fastdeliveryp_v1.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String dni;
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private String contrasena;
    private String usuario;
}