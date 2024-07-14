package com.peaje.telepass.Services.Facturacion;

import com.peaje.telepass.Models.DTOs.FacturaDTO;
import com.peaje.telepass.Models.Entity.Factura;
import com.peaje.telepass.Models.Repository.FacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class FacturaService {
    private final FacturaRepository facturaRepository;

    public List<FacturaDTO> findAll(){
        return StreamSupport.stream(facturaRepository.findAll().spliterator(),false)
                .map(this::convetToDto)
                .collect(Collectors.toList());
    }

    public FacturaDTO convetToDto(Factura factura){
        return FacturaDTO.builder()
                .id(factura.getId())
                .pagoId(factura.getPago().getId())
                .usuarioId(factura.getUsuario().getId())
                .detalle(factura.getDetalle())
                .fechaFactura(factura.getFechaFactura())
                .build();
    }
}
