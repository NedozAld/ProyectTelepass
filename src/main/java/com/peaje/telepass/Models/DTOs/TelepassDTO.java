package com.peaje.telepass.Models.DTOs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelepassDTO {
    private Long id;
    private Long usuarioId;
    private Double saldo;
    private Boolean activo;
    private Long tipoPagoId;
    private Long vehiculoId;
}
