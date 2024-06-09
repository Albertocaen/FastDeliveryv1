package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.Float;
import java.lang.String;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.proyecto.fastdeliveryp_v1.entity.Vehiculo;

@Data
@EqualsAndHashCode(callSuper = true)
public class CocheDto extends VehiculoDto {
  private Float capacidadCarga;
  private String tipoCombustible;
}
