package com.peaje.telepass.Models.DTOs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehiculoListDTO {

    private Long id;
    private String modelo;
    private String marca;
    private String placa;
    private String color;
    private String categoriaId;
    private Long usuarioId;
}
