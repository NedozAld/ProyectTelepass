package com.peaje.telepass.Controllers.Tarifas;
// Controlador de Zona O Peaje
import com.peaje.telepass.Models.DTOs.ZonaDTO;
import com.peaje.telepass.Services.Tarifas.ZonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zonas")
@RequiredArgsConstructor
public class ZonaController {

    private final ZonaService zonaService;

    @PostMapping("/crear")
    public ResponseEntity<ZonaDTO> crearZona(@RequestBody ZonaDTO zonaDTO) {
        ZonaDTO nuevaZona = zonaService.save(zonaDTO);
        return ResponseEntity.ok(nuevaZona);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ZonaDTO> actualizarZona(@PathVariable Long id, @RequestBody ZonaDTO zonaDTO) {
        ZonaDTO zonaActualizada = zonaService.update(id, zonaDTO);
        return ResponseEntity.ok(zonaActualizada);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarZona(@PathVariable Long id) {
        zonaService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ZonaDTO>> obtenerTodasLasZonas() {
        List<ZonaDTO> zonas = zonaService.findAll();
        return ResponseEntity.ok(zonas);
    }
}
