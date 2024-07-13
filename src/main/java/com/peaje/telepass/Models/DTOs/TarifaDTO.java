package com.peaje.telepass.Models.DTOs;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarifaDTO {
    private Long id;
    private Double monto;
    private Long vehiculoId;
    private Long zonaId;
}
