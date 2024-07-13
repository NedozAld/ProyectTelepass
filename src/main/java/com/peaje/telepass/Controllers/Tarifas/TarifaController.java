package com.peaje.telepass.Controllers.Tarifas;


import com.peaje.telepass.Models.DTOs.TarifaDTO;
import com.peaje.telepass.Services.Tarifas.TarifaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarifas")
@RequiredArgsConstructor
public class TarifaController {
    private final TarifaService tarifaService;

    @PostMapping("/crear")
    public ResponseEntity<TarifaDTO> crearTarifa(@RequestBody TarifaDTO tarifaDTO) {
        TarifaDTO nuevaTarifa = tarifaService.save(tarifaDTO);
        return ResponseEntity.ok(nuevaTarifa);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<TarifaDTO> actualizarTarifa(@PathVariable Long id, @RequestBody TarifaDTO tarifaDTO) {
        TarifaDTO tarifaActualizada = tarifaService.update(id, tarifaDTO);
        return ResponseEntity.ok(tarifaActualizada);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarTarifa(@PathVariable Long id) {
        tarifaService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TarifaDTO>> obtenerTarifas() {
        List<TarifaDTO> tarifas = tarifaService.findAll();
        return ResponseEntity.ok(tarifas);
    }

    @GetMapping("/vehiculo/{vehiculoId}/zona/{zonaId}")
    public ResponseEntity<List<TarifaDTO>> obtenerTarifasPorVehiculoYZona(@PathVariable Long vehiculoId, @PathVariable Long zonaId) {
        List<TarifaDTO> tarifas = tarifaService.obtenerTarifasPorVehiculoYZona(vehiculoId, zonaId);
        return ResponseEntity.ok(tarifas);
    }
}
