package com.peaje.telepass.Controllers.Telepass;

import com.peaje.telepass.Models.DTOs.TelepassDTO;
import com.peaje.telepass.Models.DTOs.TipoPagoDTO;
import com.peaje.telepass.Services.Telepass.TelepassService;
import com.peaje.telepass.Services.Telepass.TipoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/telepass")
@RequiredArgsConstructor
public class TelepassController {

    private final TelepassService telepassService;

    @PostMapping("/crear")
    public ResponseEntity<TelepassDTO> crearTelepass(@RequestBody TelepassDTO telepassDTO) {
        TelepassDTO nuevoTelepass = telepassService.save(telepassDTO);
        return ResponseEntity.ok(nuevoTelepass);
    }

    @PutMapping("/recargar/{id}")
    public ResponseEntity<TelepassDTO> recargarTelepass(@PathVariable Long id, @RequestParam Double montoRecarga) {
        TelepassDTO telepassRecargado = telepassService.recargar(id, montoRecarga);
        return ResponseEntity.ok(telepassRecargado);
    }

    @PutMapping("/cambiar-titularidad/{telepassId}")
    public ResponseEntity<TelepassDTO> cambiarTitularidad(@PathVariable Long telepassId, @RequestParam Long nuevoUsuarioId) {
        TelepassDTO telepassActualizado = telepassService.cambiarTitularidad(telepassId, nuevoUsuarioId);
        return ResponseEntity.ok(telepassActualizado);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TelepassDTO>> obtenerTelepassesPorUsuarioId(@PathVariable Long usuarioId) {
        List<TelepassDTO> telepasses = telepassService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(telepasses);
    }

}
