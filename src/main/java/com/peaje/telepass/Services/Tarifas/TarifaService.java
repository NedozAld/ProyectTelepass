package com.peaje.telepass.Services.Tarifas;

import com.peaje.telepass.Models.DTOs.TarifaDTO;
import com.peaje.telepass.Models.Entity.Tarifa;
import com.peaje.telepass.Models.Entity.VehiculoCategoria;
import com.peaje.telepass.Models.Entity.Zona;
import com.peaje.telepass.Models.Repository.TarifaRepository;

import com.peaje.telepass.Models.Repository.VehiculoCategoriaRepository;
import com.peaje.telepass.Models.Repository.ZonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class TarifaService {

    private final TarifaRepository tarifaRepository;
    private  final VehiculoCategoriaRepository vehiculoRepository;
    private final ZonaRepository zonaRepository;

    public TarifaDTO save(TarifaDTO tarifaDTO) {
        VehiculoCategoria vehiculo = vehiculoRepository.findById(tarifaDTO.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));
        Zona zona = zonaRepository.findById(tarifaDTO.getZonaId())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));

        Tarifa tarifa = Tarifa.builder()
                .monto(tarifaDTO.getMonto())
                .vehiculo(vehiculo)
                .zona(zona)
                .build();

        return convertToDto(tarifaRepository.save(tarifa));
    }

    public TarifaDTO update(Long id, TarifaDTO tarifaDTO){
        Tarifa tarifaExistente = tarifaRepository.findById(id).get();

        VehiculoCategoria vehiculo = vehiculoRepository.findById(tarifaDTO.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));
        Zona zona = zonaRepository.findById(tarifaDTO.getZonaId())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));

        if(tarifaExistente != null) {
            Tarifa tarifa = Tarifa.builder()
                    .id(tarifaExistente.getId())
                    .monto(tarifaDTO.getMonto())
                    .vehiculo(vehiculo)
                    .zona(zona)
                    .build();
            return convertToDto(tarifaRepository.save(tarifa));
        }else{
            return null;
        }

    }

    public void delete(Long id) {
        tarifaRepository.deleteById(id);
    }

    public List<TarifaDTO> findAll(){
        return StreamSupport.stream(tarifaRepository.findAll().spliterator(), false)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TarifaDTO> obtenerTarifasPorVehiculoYZona(Long vehiculoId, Long zonaId) {
        VehiculoCategoria vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));
        Zona zona = zonaRepository.findById(zonaId)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));

        List<Tarifa> tarifas = tarifaRepository.findByVehiculoAndZona(vehiculo, zona);
        return tarifas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public TarifaDTO convertToDto(Tarifa tarifa) {
        return TarifaDTO.builder()
                .id(tarifa.getId())
                .monto(tarifa.getMonto())
                .vehiculoId(tarifa.getVehiculo().getId())
                .zonaId(tarifa.getZona().getId())
                .build();
    }
}
