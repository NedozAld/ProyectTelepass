package com.peaje.telepass.Models.DTOs;

import com.peaje.telepass.Models.Entity.Usuario;
import com.peaje.telepass.Models.Entity.Vehiculo;
import com.peaje.telepass.Models.Entity.Zona;

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
