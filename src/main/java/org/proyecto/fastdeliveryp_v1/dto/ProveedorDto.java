package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.Integer;
import java.lang.String;


import lombok.Data;

@Data
public class ProveedorDto {
  private Integer id;
  private String nombreEmpresa;
  private String telefono;
  private String email;
}
