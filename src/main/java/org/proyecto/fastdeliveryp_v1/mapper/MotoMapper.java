package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;

import org.proyecto.fastdeliveryp_v1.dto.MotoDto;
import org.proyecto.fastdeliveryp_v1.entity.Moto;

@Mapper(componentModel = "spring")
public interface MotoMapper {
    MotoDto toDto(Moto entity);

    Moto toEntity(MotoDto dto);
}
