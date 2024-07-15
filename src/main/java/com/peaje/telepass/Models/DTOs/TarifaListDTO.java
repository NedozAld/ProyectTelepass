package com.peaje.telepass.Models.DTOs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarifaListDTO {
    private Long id;
    private Double monto;
    private String vehiculo;
    private String zona;
}
