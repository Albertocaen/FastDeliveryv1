package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.proyecto.fastdeliveryp_v1.dto.PersonaDto;
import org.proyecto.fastdeliveryp_v1.entity.Producto;
@Mapper(componentModel = "spring")
public interface ProductoMapper {
  PersonaDto toDto(Producto entity);

  Producto toEntity(PersonaDto dto);
}
