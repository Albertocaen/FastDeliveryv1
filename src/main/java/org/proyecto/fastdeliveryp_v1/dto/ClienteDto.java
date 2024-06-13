package org.proyecto.fastdeliveryp_v1.dto;

import java.lang.String;

import lombok.Data;


@Data
public class ClienteDto {
  private String dni;
  private PersonaDto persona;
}
