package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.proyecto.fastdeliveryp_v1.dto.RepartidorDto;
import org.proyecto.fastdeliveryp_v1.entity.Repartidor;

@Mapper(componentModel = "spring", uses = {VehiculoMapper.class})
public interface RepartidorMapper {
    @Mapping(source = "placaVehiculo", target = "vehiculo")
    RepartidorDto toDto(Repartidor entity);

    @Mapping(source = "vehiculo", target = "placaVehiculo")
    Repartidor toEntity(RepartidorDto dto);
}
