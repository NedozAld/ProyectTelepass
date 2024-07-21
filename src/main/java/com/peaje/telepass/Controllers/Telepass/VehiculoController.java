package com.peaje.telepass.Controllers.Telepass;

// Controlador de Vehiculo
import com.peaje.telepass.Models.DTOs.VehiculoDTO;
import com.peaje.telepass.Models.DTOs.VehiculoListDTO;
import com.peaje.telepass.Models.Entity.Vehiculo;
import com.peaje.telepass.Models.Repository.VehiculoRepository;
import com.peaje.telepass.Services.Telepass.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;
    private final VehiculoRepository vehiculoRepository;

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
    public ResponseEntity<List<VehiculoListDTO>> findAll() {
        List<VehiculoListDTO> vehiculos = vehiculoService.findAll();
        return ResponseEntity.ok(vehiculos);
    }

    @GetMapping("/validar-placa")
    public ResponseEntity<Boolean> validarPlaca(@RequestParam String placa) {
        boolean existe = vehiculoRepository.existsByPlaca(placa);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<VehiculoListDTO>> getVehiculosByUsuarioId(@PathVariable Long usuarioId) {
        List<VehiculoListDTO> vehiculos = vehiculoService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(vehiculos);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehiculoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private VehiculoDTO convertToDTO(Vehiculo vehiculo) {
        VehiculoDTO dto = new VehiculoDTO();
        dto.setId(vehiculo.getId());
        dto.setModelo(vehiculo.getModelo());
        dto.setMarca(vehiculo.getMarca());
        dto.setPlaca(vehiculo.getPlaca());
        dto.setColor(vehiculo.getColor());
        dto.setCategoriaId(vehiculo.getCategoria().getId());
        dto.setUsuarioId(vehiculo.getUsuario().getId());
        return dto;
    }
}
