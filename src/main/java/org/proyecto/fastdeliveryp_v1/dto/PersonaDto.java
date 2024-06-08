package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.String;
import lombok.Data;
import org.proyecto.fastdeliveryp_v1.entity.Admin;
import org.proyecto.fastdeliveryp_v1.entity.Cliente;
import org.proyecto.fastdeliveryp_v1.entity.Repartidor;

@Data
public class PersonaDto {
  private String dni;

  private String email;

  private String nombre;

  private String apellido;

  private String telefono;

  private String contraseña;

  private String usuario;

  private Cliente cliente;

  private Repartidor repartidor;

  private Admin admin;
}
