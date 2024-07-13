package com.peaje.telepass.Services.Tarifas;


import com.peaje.telepass.Models.DTOs.VehiculoCategoriaDTO;
import com.peaje.telepass.Models.Entity.VehiculoCategoria;
import com.peaje.telepass.Models.Repository.VehiculoCategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class VehiculoCategoriaService {

    private final VehiculoCategoriaRepository vehiculoCategoriaRepository;

    public VehiculoCategoriaDTO save(VehiculoCategoriaDTO vehiculoCategoriaDTO){
        VehiculoCategoria vehiculoCategoria = VehiculoCategoria.builder()
                .tipo(vehiculoCategoriaDTO.getTipo())
                .build();
        return convertToDTO(vehiculoCategoriaRepository.save(vehiculoCategoria));
    }

    public VehiculoCategoriaDTO update(Long id, VehiculoCategoriaDTO vehiculoCategoriaDTO){
        VehiculoCategoria existeCategoria = vehiculoCategoriaRepository.findById(id).get();

        if(existeCategoria != null){
            VehiculoCategoria vehiculoCategoria = VehiculoCategoria.builder()
                    .id(existeCategoria.getId())
                    .tipo(vehiculoCategoriaDTO.getTipo())
                    .build();
            return  convertToDTO(vehiculoCategoriaRepository.save(vehiculoCategoria));

        }else{
            return null;
        }


    }

    public List<VehiculoCategoriaDTO> findAll(){
        return StreamSupport.stream(vehiculoCategoriaRepository.findAll().spliterator(), false)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void delete(Long id){
        vehiculoCategoriaRepository.deleteById(id);
    }
    public VehiculoCategoriaDTO convertToDTO(VehiculoCategoria vehiculoCategoria){
        return VehiculoCategoriaDTO.builder()
                .id(vehiculoCategoria.getId())
                .tipo(vehiculoCategoria.getTipo())
                .build();
    }
}
