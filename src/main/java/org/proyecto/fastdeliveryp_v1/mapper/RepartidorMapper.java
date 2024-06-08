package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.proyecto.fastdeliveryp_v1.dto.RepartidorDto;
import org.proyecto.fastdeliveryp_v1.entity.Repartidor;
@Mapper(componentModel = "spring")
public interface RepartidorMapper {
  RepartidorDto toDto(Repartidor entity);

  Repartidor toEntity(RepartidorDto dto);
}
