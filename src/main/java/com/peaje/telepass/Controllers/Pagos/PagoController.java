package com.peaje.telepass.Controllers.Pagos;

import com.peaje.telepass.Models.DTOs.PagoDTO;
import com.peaje.telepass.Services.Pagos.PagoServicio;
import com.peaje.telepass.Services.Telepass.TipoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
