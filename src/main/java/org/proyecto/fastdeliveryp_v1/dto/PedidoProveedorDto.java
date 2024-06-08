package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.Integer;
import java.lang.String;
import java.time.LocalDate;
import lombok.Data;
import org.proyecto.fastdeliveryp_v1.entity.Admin;
import org.proyecto.fastdeliveryp_v1.entity.Proveedor;

@Data
public class PedidoProveedorDto {
  private Integer id;

  private Integer cantidad;

  private LocalDate fechaPedido;

  private String estado;

  private Proveedor idProveedorPedido;

  private Admin dniAdminPedido;
}
