package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.proyecto.fastdeliveryp_v1.dto.PersonaDto;
import org.proyecto.fastdeliveryp_v1.entity.Persona;
@Mapper(componentModel = "spring")
public interface PersonaMapper {
  PersonaDto toDto(Persona entity);

  Persona toEntity(PersonaDto dto);
}
