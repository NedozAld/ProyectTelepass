package com.peaje.telepass.Controllers.Pagos;

import com.peaje.telepass.Models.DTOs.PagoDTO;
import com.peaje.telepass.Models.DTOs.PagoLisDTO;
import com.peaje.telepass.Models.DTOs.VehiculoCategoriaDTO;
import com.peaje.telepass.Services.Pagos.PagoServicio;
import com.peaje.telepass.Services.Telepass.TipoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoServicio pagoService;

    @PostMapping("/realizarPago")
    public ResponseEntity<PagoDTO> realizarPago(@RequestParam Long vehiculoId, @RequestParam Long zonaId) {
        PagoDTO pagoDTO = pagoService.realizarPago(vehiculoId, zonaId);
        return ResponseEntity.ok(pagoDTO);
    }

    @PostMapping("/realizarPagoEfectivo")
    public ResponseEntity<PagoDTO> realizarPagoEfectivo(@RequestParam String placa, @RequestParam Long zonaId) {
        PagoDTO pagoDTO = pagoService.realizarPagoEfectivo(placa, zonaId);
        return ResponseEntity.ok(pagoDTO);
    }

    @GetMapping
    public ResponseEntity<List<PagoLisDTO>> obtenerTodasLasTransacciones() {
        List<PagoLisDTO> transacciones = pagoService.findAll();
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/tarifaEfectivo")
    public ResponseEntity<Double> ObtenerTarifa(@RequestParam String placa, @RequestParam Long zonaId){
        Double tarifa = pagoService.ObtenerTarifa(placa,zonaId);
        return ResponseEntity.ok(tarifa);
    }
}
