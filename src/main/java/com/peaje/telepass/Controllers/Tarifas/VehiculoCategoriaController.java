package com.peaje.telepass.Controllers.Tarifas;

import com.peaje.telepass.Models.DTOs.VehiculoCategoriaDTO;
import com.peaje.telepass.Services.Tarifas.VehiculoCategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculo-categorias")
@RequiredArgsConstructor
public class VehiculoCategoriaController {

    private final VehiculoCategoriaService vehiculoCategoriaService;

    @PostMapping("/crear")
    public ResponseEntity<VehiculoCategoriaDTO> crearVehiculoCategoria(@RequestBody VehiculoCategoriaDTO vehiculoCategoriaDTO) {
        VehiculoCategoriaDTO nuevaCategoria = vehiculoCategoriaService.save(vehiculoCategoriaDTO);
        return ResponseEntity.ok(nuevaCategoria);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<VehiculoCategoriaDTO> actualizarVehiculoCategoria(@PathVariable Long id, @RequestBody VehiculoCategoriaDTO vehiculoCategoriaDTO) {
        VehiculoCategoriaDTO categoriaActualizada = vehiculoCategoriaService.update(id, vehiculoCategoriaDTO);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarVehiculoCategoria(@PathVariable Long id) {
        vehiculoCategoriaService.delete(id);
        return ResponseEntity.ok().build();
    }

    
    @GetMapping
    public ResponseEntity<List<VehiculoCategoriaDTO>> obtenerTodasLasCategorias() {
        List<VehiculoCategoriaDTO> categorias = vehiculoCategoriaService.findAll();
        return ResponseEntity.ok(categorias);
    }
}
