package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.proyecto.fastdeliveryp_v1.dto.VehiculoDto;
import org.proyecto.fastdeliveryp_v1.entity.Vehiculo;
@Mapper(componentModel = "spring")
public interface VehiculoMapper {
  VehiculoDto toDto(Vehiculo entity);

  Vehiculo toEntity(VehiculoDto dto);
}
