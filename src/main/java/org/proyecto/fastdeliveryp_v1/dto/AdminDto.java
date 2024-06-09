package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.String;
import java.util.List;
import lombok.Data;
import org.proyecto.fastdeliveryp_v1.entity.Persona;

@Data
public class AdminDto {
  private String dniAdmin;
  private String nombre;
  private String apellido;
}
