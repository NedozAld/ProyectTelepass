package com.peaje.telepass.Controllers.Telepass;


import com.peaje.telepass.Models.DTOs.VehiculoDTO;
import com.peaje.telepass.Services.Telepass.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PostMapping("/crear")
    public ResponseEntity<VehiculoDTO> save(@RequestBody VehiculoDTO vehiculoDTO) {
        VehiculoDTO savedVehiculo = vehiculoService.save(vehiculoDTO);
        return ResponseEntity.ok(savedVehiculo);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<VehiculoDTO> update(@PathVariable Long id, @RequestBody VehiculoDTO vehiculoDTO) {
        VehiculoDTO updatedVehiculo = vehiculoService.update(id, vehiculoDTO);
        return ResponseEntity.ok(updatedVehiculo);
    }

    @GetMapping
    public ResponseEntity<List<VehiculoDTO>> findAll() {
        List<VehiculoDTO> vehiculos = vehiculoService.findAll();
        return ResponseEntity.ok(vehiculos);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehiculoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
