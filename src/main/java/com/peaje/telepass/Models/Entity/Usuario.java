package com.peaje.telepass.Models.Entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nombre;
  private String apellido;
  private String cedula;
  private String correo;
  private String contrasena;
  private LocalDate fechaNacimiento;
  private String genero;
  @Enumerated(EnumType.ORDINAL)
  private Role role;
}
