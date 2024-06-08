package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.proyecto.fastdeliveryp_v1.dto.CocheDto;
import org.proyecto.fastdeliveryp_v1.entity.Coche;
@Mapper(componentModel = "spring")
public interface CocheMapper {
  CocheDto toDto(Coche entity);

  Coche toEntity(CocheDto dto);
}
