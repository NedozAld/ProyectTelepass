package com.peaje.telepass.Models.Entity;

import jakarta.persistence.*;

@Entity
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double monto;

    @ManyToOne
    private Vehiculo vehiculo;

    @ManyToOne
    private Zona zona;

}
