package com.peaje.telepass.Controllers.Telepass;

import com.peaje.telepass.Models.DTOs.TipoPagoDTO;
import com.peaje.telepass.Services.Telepass.TipoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipopago")
@RequiredArgsConstructor
public class TipoPagoController {
    private final TipoPagoService tipoPagoService;

    @PostMapping
    public ResponseEntity<TipoPagoDTO> createTipoPago(@RequestBody TipoPagoDTO tipoPagoDTO) {
        TipoPagoDTO nuevoTipoPago = tipoPagoService.save(tipoPagoDTO);
        return ResponseEntity.ok(nuevoTipoPago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoPagoDTO> updateTipoPago(@PathVariable Long id, @RequestBody TipoPagoDTO tipoPagoDTO) {
        TipoPagoDTO actualizadoTipoPago = tipoPagoService.update(id, tipoPagoDTO);
        return ResponseEntity.ok(actualizadoTipoPago);
    }

    @GetMapping
    public ResponseEntity<List<TipoPagoDTO>> getAllTipoPagos() {
        List<TipoPagoDTO> tipoPagos = tipoPagoService.findAll();
        return ResponseEntity.ok(tipoPagos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoPago(@PathVariable Long id) {
        tipoPagoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
