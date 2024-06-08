package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.proyecto.fastdeliveryp_v1.dto.StockDto;
import org.proyecto.fastdeliveryp_v1.entity.Stock;
@Mapper(componentModel = "spring")
public interface StockMapper {
  StockDto toDto(Stock entity);

  Stock toEntity(StockDto dto);
}
