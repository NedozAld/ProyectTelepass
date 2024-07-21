package com.peaje.telepass.Models.DTOs;
// clases dto


import com.peaje.telepass.Models.Entity.TipoPago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelepassLisDTO {
    private Long id;
    private String usuarioId;
    private Double saldo;
    private Boolean activo;
    private String nombre;
    private Long tipoPagoId;
    private String vehiculoId;
}
