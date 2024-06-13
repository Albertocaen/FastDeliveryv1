package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.Float;
import java.lang.Integer;
import java.lang.String;
import lombok.Data;

@Data
public class ProductoDto {
  private Integer id;
  private String nombre;
  private String descripcion;
  private Float precio;
}
