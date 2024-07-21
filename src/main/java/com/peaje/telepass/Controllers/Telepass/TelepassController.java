package com.peaje.telepass.Controllers.Telepass;
// Controlador de Telepass
import com.peaje.telepass.Models.DTOs.TelepassDTO;
import com.peaje.telepass.Models.DTOs.TelepassLisDTO;
import com.peaje.telepass.Services.Telepass.TelepassService;

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
    public ResponseEntity<TelepassDTO> recargarTelepass(@PathVariable Long id, @RequestParam Double montoRecarga, @RequestParam Long idPago) {
        TelepassDTO telepassRecargado = telepassService.recargar(id, montoRecarga, idPago);
        return ResponseEntity.ok(telepassRecargado);
    }

    @PutMapping("/aprobar/{id}")
    public ResponseEntity<TelepassDTO> recargarTelepass(@PathVariable Long id) {
        TelepassDTO telepassRecargado = telepassService.aprobarTelepass(id);
        return ResponseEntity.ok(telepassRecargado);
    }


    @PutMapping("/cambiar-titularidad/{telepassId}")
    public ResponseEntity<TelepassDTO> cambiarTitularidad(@PathVariable Long telepassId, @RequestParam Long nuevoUsuarioId) {
        TelepassDTO telepassActualizado = telepassService.cambiarTitularidad(telepassId, nuevoUsuarioId);
        return ResponseEntity.ok(telepassActualizado);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TelepassLisDTO>> obtenerTelepassesPorUsuarioId(@PathVariable Long usuarioId) {
        List<TelepassLisDTO> telepasses = telepassService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(telepasses);
    }
    @GetMapping("/telepassActivo/{usuarioId}")
    public ResponseEntity<List<TelepassLisDTO>> obtenerTelepassesActivo(@PathVariable Long usuarioId) {
        List<TelepassLisDTO> telepasses = telepassService.findByUsuarioIdTrue(usuarioId);
        return ResponseEntity.ok(telepasses);
    }
    @GetMapping
    public ResponseEntity<List<TelepassLisDTO>> obtenerTelepasses() {
        List<TelepassLisDTO> telepasses = telepassService.findAll();
        return ResponseEntity.ok(telepasses);
    }

}
