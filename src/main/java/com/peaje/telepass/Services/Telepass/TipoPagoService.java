package com.peaje.telepass.Services.Telepass;

import com.peaje.telepass.Models.DTOs.TipoPagoDTO;
import com.peaje.telepass.Models.Entity.TipoPago;
import com.peaje.telepass.Models.Repository.TipoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class TipoPagoService {
    private final TipoPagoRepository tipoPagoRepository;

    public TipoPagoDTO save(TipoPagoDTO tipoPagoDTO){
        TipoPago tipoPago = TipoPago.builder()
                .descripcion(tipoPagoDTO.getDescripcion())
                .build();
        return convertToDto(tipoPagoRepository.save(tipoPago));
    }

    public TipoPagoDTO update(Long id, TipoPagoDTO tipoPagoDTO){
        TipoPago existePago = tipoPagoRepository.findById(id).get();
        if(existePago != null){

            TipoPago tipoPago = TipoPago.builder()
                    .id(existePago.getId())
                    .descripcion(tipoPagoDTO.getDescripcion())
                    .build();
            return convertToDto(tipoPagoRepository.save(tipoPago));
        }else{
            return null;
        }
    }

    public List<TipoPagoDTO> findAll(){
        return StreamSupport.stream(tipoPagoRepository.findAll().spliterator(),false)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id){
        tipoPagoRepository.deleteById(id);
    }


    public TipoPagoDTO convertToDto(TipoPago tipoPago){
        return TipoPagoDTO.builder()
                .id(tipoPago.getId())
                .descripcion(tipoPago.getDescripcion())
                .build();
    }
}
