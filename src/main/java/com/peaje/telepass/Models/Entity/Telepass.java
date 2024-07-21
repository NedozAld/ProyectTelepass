package com.peaje.telepass.Models.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Telepass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Usuario usuario;
    private Double saldo;
    private Boolean activo;
    private String nombre;
    @ManyToOne(optional = true)
    @JoinColumn(name = "tipo_pago_id")
    private TipoPago tipoPago;
    @ManyToOne
    private  Vehiculo vehiculo;
}
