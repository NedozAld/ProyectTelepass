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
public class PagoDTO {
    private Long id;
    private Long zonaId;
    private Long vehiculoId;
    private Long usuarioId;
    private Double monto;
    private LocalDate fechaPago;

}
