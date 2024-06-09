package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.Integer;
import java.time.LocalDate;
import lombok.Data;
import org.proyecto.fastdeliveryp_v1.entity.Admin;
import org.proyecto.fastdeliveryp_v1.entity.Producto;

@Data
public class StockDto {
  private Integer id;
  private Integer cantidad;
  private LocalDate fechaIngreso;
  private ProductoDto idProducto;
}
