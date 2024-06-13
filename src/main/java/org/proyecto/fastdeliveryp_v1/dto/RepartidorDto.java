package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.Float;
import java.lang.Integer;
import java.lang.String;

import lombok.Data;


@Data
public class RepartidorDto {
  private String dniRepartidor;
  private PersonaDto persona;
  private Float calificacion;
  private String horarioTrabajo;
  private String estadoDeDisponibilidad;
  private Integer cantidadPedidos;
  private VehiculoDto vehiculo;
}
