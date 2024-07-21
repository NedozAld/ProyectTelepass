package com.peaje.telepass.Models.DTOs;

// clases dto

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoPagoDTO {

    private Long id;
    private String descripcion;
}
