package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.Float;
import java.lang.Integer;
import java.lang.String;
import java.time.LocalDate;
import lombok.Data;


@Data
public class PedidoClienteDto {
  private Integer id;
  private Integer cantidad;
  private LocalDate fechaPedido;
  private Float peso;
  private String estado;
  private ClienteDto cliente;
  private RepartidorDto repartidor;
  private ProductoDto producto;
}
