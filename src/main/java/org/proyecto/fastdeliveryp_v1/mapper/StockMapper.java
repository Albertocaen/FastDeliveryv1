package org.proyecto.fastdeliveryp_v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.proyecto.fastdeliveryp_v1.dto.StockDto;
import org.proyecto.fastdeliveryp_v1.entity.Stock;
@Mapper(componentModel = "spring")
public interface StockMapper {
  @Mapping(source = "idProducto", target = "idProducto")
  StockDto toDto(Stock stock);

  @Mapping(source = "idProducto", target = "idProducto")
  Stock toEntity(StockDto stockDto);
}
