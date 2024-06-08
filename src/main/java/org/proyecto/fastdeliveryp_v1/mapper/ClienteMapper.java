package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.proyecto.fastdeliveryp_v1.dto.ClienteDto;
import org.proyecto.fastdeliveryp_v1.entity.Cliente;
@Mapper(componentModel = "spring")
public interface ClienteMapper {
  ClienteDto toDto(Cliente entity);

  Cliente toEntity(ClienteDto dto);
}
