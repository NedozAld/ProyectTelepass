package com.peaje.telepass.Models.Entity;

//Hello
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
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Zona zona;
    @ManyToOne
    private Vehiculo vehiculo;
    @ManyToOne
    private Usuario usuario;
    private Double monto;
    private LocalDate fechaPago;


}
