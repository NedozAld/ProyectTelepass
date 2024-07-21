package com.peaje.telepass.Models.DTOs;
// clases dto

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagoLisDTO {
    private Long id;
    private String zona;
    private String vehiculo;
    private String usuario;
    private Double monto;
    private LocalDate fechaPago;

}
