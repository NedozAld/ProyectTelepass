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
public class VehiculoDTO {

    private Long id;
    private String modelo;
    private String marca;
    private String placa;
    private String color;
    private Long categoriaId;
    private Long usuarioId;
}
