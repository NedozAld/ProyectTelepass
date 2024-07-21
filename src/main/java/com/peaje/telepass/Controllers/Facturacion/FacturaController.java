package com.peaje.telepass.Controllers.Facturacion;
//controlador de Factura
import com.peaje.telepass.Models.DTOs.FacturaDTO;
import com.peaje.telepass.Services.Facturacion.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaController {
    private final FacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> findAll() {
        List<FacturaDTO> facturas = facturaService.findAll();
        return ResponseEntity.ok(facturas);
    }
}
