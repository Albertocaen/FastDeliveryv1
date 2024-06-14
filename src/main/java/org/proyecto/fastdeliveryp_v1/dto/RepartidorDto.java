package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.Float;
import java.lang.Integer;
import java.lang.String;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RepartidorDto {
  @NotEmpty(message = "El DNI del repartidor no puede estar vacío")
  @Size(max = 20, message = "El DNI del repartidor no puede tener más de 20 caracteres")
  private String dniRepartidor;
  private PersonaDto persona;

  @DecimalMin(value = "0.0", inclusive = true, message = "La calificación debe ser positiva")
  private Float calificacion;

  @Size(max = 100, message = "El horario de trabajo no puede tener más de 100 caracteres")
  private String horarioTrabajo;

  @Size(max = 50, message = "El estado de disponibilidad no puede tener más de 50 caracteres")
  private String estadoDeDisponibilidad;

  private Integer cantidadPedidos;
  private VehiculoDto vehiculo;
}
