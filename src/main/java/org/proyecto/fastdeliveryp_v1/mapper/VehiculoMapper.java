package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.proyecto.fastdeliveryp_v1.dto.VehiculoDto;
import org.proyecto.fastdeliveryp_v1.entity.Vehiculo;
@Mapper(componentModel = "spring")
public interface VehiculoMapper {
  @Mapping(target = "placaVehiculo", source = "placaVehiculo")
  @Mapping(target = "color", source = "color")
  VehiculoDto toDto(Vehiculo vehiculo);

  @Mapping(target = "placaVehiculo", source = "placaVehiculo")
  @Mapping(target = "color", source = "color")
  Vehiculo toEntity(VehiculoDto vehiculoDto);
}
