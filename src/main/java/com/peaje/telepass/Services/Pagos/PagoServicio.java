package com.peaje.telepass.Services.Pagos;


import com.peaje.telepass.Models.DTOs.PagoDTO;
import com.peaje.telepass.Models.DTOs.TelepassDTO;
import com.peaje.telepass.Models.Entity.*;
import com.peaje.telepass.Models.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PagoServicio {

    private final PagoRepository pagoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final TelepassRepository telepassRepository;
    private  final ZonaRepository zonaRepository;
    private final TarifaRepository tarifaRepository;

    public PagoDTO realizarPago(Long vehiculoId, Long zonaId) {
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

        Telepass telepass = telepassRepository.findByVehiculoId(vehiculoId)
                .orElseThrow(() -> new RuntimeException("Telepass no encontrado para el vehículo"));

        Zona zona = zonaRepository.findById(zonaId)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));

        VehiculoCategoria categoria = vehiculo.getCategoria();

        Tarifa tarifa = tarifaRepository.findByVehiculoAndZona(categoria, zona)
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada"));

        Double monto = tarifa.getMonto();

        if (telepass.getSaldo() < monto) {
            throw new RuntimeException("Saldo insuficiente");
        }

        // Descontar saldo
        telepass.setSaldo(telepass.getSaldo() - monto);
        telepassRepository.save(telepass);

        // Registrar el pago
        Pago pago = Pago.builder()
                .zona(zona)
                .vehiculo(vehiculo)
                .usuario(telepass.getUsuario())
                .monto(monto)
                .fechaPago(LocalDate.now())
                .build();



        return convertToDto(pagoRepository.save(pago));
    }




    public PagoDTO convertToDto(Pago pago){
        return PagoDTO.builder()
                .id(pago.getId())
                .zonaId(pago.getZona().getId())
                .vehiculoId(pago.getVehiculo().getId())
                .usuarioId(pago.getUsuario().getId())
                .monto(pago.getMonto())
                .fechaPago(pago.getFechaPago())
                .build();
    }
}
