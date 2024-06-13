package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.Integer;
import java.lang.String;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;


@Data
public class PedidoProveedorDto {
  private Integer id;
  private Integer cantidad;
  private LocalDate fechaPedido;
  private String estado;
  private ProveedorDto idProveedorPedido;
  private AdminDto dniAdminPedido;
  private List<PedidoProveedorProductoInfo> productos;
}
