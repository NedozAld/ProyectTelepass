package com.peaje.telepass.Services.Tarifas;


import com.peaje.telepass.Models.DTOs.ZonaDTO;
import com.peaje.telepass.Models.Entity.Zona;
import com.peaje.telepass.Models.Repository.ZonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ZonaService {

    private final ZonaRepository zonaRepository;

    public ZonaDTO save(ZonaDTO zonaDTO){
        Zona zona = Zona.builder()
                .nombre(zonaDTO.getNombre())
                .build();
        return convertToDto(zonaRepository.save(zona));
    }

    public ZonaDTO update(Long id, ZonaDTO zonaDTO){
        Zona existeZona = zonaRepository.findById(id).get();
        if(existeZona != null){
            Zona zona = Zona.builder()
                    .id(existeZona.getId())
                    .nombre(zonaDTO.getNombre())
                    .build();
            return convertToDto(zonaRepository.save(zona));
        }else{
            return null;
        }
    }

    public List<ZonaDTO> findAll(){
        return StreamSupport.stream(zonaRepository.findAll().spliterator(),false)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id){

        zonaRepository.deleteById(id);
    }

    public ZonaDTO convertToDto(Zona zona){
        return ZonaDTO.builder()
                .id(zona.getId())
                .nombre(zona.getNombre())
                .build();
    }
}
