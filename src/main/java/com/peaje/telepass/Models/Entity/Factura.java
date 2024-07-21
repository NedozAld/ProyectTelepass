package com.peaje.telepass.Models.Entity;
// clases dto

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Pago pago;
    @ManyToOne
    private Usuario usuario;
    private String detalle;
    private LocalDate fechaFactura;
}
