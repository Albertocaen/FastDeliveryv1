package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.proyecto.fastdeliveryp_v1.dto.ClienteDto;
import org.proyecto.fastdeliveryp_v1.entity.Cliente;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    @Mapping(source = "persona.dni", target = "dni")
    ClienteDto toDto(Cliente entity);

    @Mapping(source = "dni", target = "persona.dni")
    Cliente toEntity(ClienteDto dto);
}
