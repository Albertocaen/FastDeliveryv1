package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.Integer;
import java.lang.String;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.proyecto.fastdeliveryp_v1.entity.Vehiculo;

/**
 * DTO para una moto, que extiende de VehiculoDto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MotoDto extends VehiculoDto {
  private Integer cilindrada;
}
