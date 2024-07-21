package com.peaje.telepass.Models.DTOs;
// clases dto

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehiculoCategoriaDTO {
    private Long id;
    private String tipo;
}
