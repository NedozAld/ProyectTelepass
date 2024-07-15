package com.peaje.telepass.Models.DTOs;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
  private Long id;
  private String nombre;
  private String apellido;
  private String cedula;
  private String correo;
  private String contrasena;
  private LocalDate fechaNacimiento;
  private String genero;
  private String role;
}
