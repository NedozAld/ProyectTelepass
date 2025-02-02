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
public class FacturaDTO {

    private Long id;
    private Long pagoId;
    private Long usuarioId;
    private String detalle;
    private LocalDate fechaFactura;
}
