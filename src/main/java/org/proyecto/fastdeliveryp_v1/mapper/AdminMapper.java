package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.proyecto.fastdeliveryp_v1.dto.AdminDto;
import org.proyecto.fastdeliveryp_v1.entity.Admin;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    @Mapping(source = "dniAdmin", target = "dniAdmin")
    @Mapping(source = "persona.nombre", target = "nombre")
    @Mapping(source = "persona.apellido", target = "apellido")
    AdminDto toDto(Admin admin);

    @Mapping(source = "dniAdmin", target = "dniAdmin")
    @Mapping(target = "persona.nombre", source = "nombre")
    @Mapping(target = "persona.apellido", source = "apellido")
    Admin toEntity(AdminDto adminDto);
}
