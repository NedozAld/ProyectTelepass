package com.peaje.telepass.Services.Telepass;

import com.peaje.telepass.Models.DTOs.VehiculoDTO;
import com.peaje.telepass.Models.Entity.Usuario;
import com.peaje.telepass.Models.Entity.Vehiculo;
import com.peaje.telepass.Models.Entity.VehiculoCategoria;
import com.peaje.telepass.Models.Repository.UsuarioRepository;
import com.peaje.telepass.Models.Repository.VehiculoCategoriaRepository;
import com.peaje.telepass.Models.Repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class VehiculoService {
    private final VehiculoRepository vehiculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final VehiculoCategoriaRepository vehiculoCategoriaRepository;

    public VehiculoDTO save(VehiculoDTO vehiculoDTO){
        Usuario usuario = usuarioRepository.findById(vehiculoDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));
        VehiculoCategoria vehiculoCategoria = vehiculoCategoriaRepository.findById(vehiculoDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrado"));

        Vehiculo vehiculo = Vehiculo.builder()
                .modelo(vehiculoDTO.getModelo())
                .marca(vehiculoDTO.getMarca())
                .placa(vehiculoDTO.getPlaca())
                .color(vehiculoDTO.getColor())
                .categoria(vehiculoCategoria)
                .usuario(usuario)
                .build();
        return convertToDTO(vehiculoRepository.save(vehiculo));
    }

    public VehiculoDTO update(Long id, VehiculoDTO vehiculoDTO){
        Vehiculo existeVehiculo = vehiculoRepository.findById(id).get();
        Usuario usuario = usuarioRepository.findById(vehiculoDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));
        VehiculoCategoria vehiculoCategoria = vehiculoCategoriaRepository.findById(vehiculoDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrado"));

        if(existeVehiculo != null){
            Vehiculo vehiculo = Vehiculo.builder()
                    .id(existeVehiculo.getId())
                    .modelo(vehiculoDTO.getModelo())
                    .marca(vehiculoDTO.getMarca())
                    .placa(vehiculoDTO.getPlaca())
                    .color(vehiculoDTO.getColor())
                    .categoria(vehiculoCategoria)
                    .usuario(usuario)
                    .build();
            return convertToDTO(vehiculoRepository.save(vehiculo));

        }else{
            return null;
        }
    }

    public List<VehiculoDTO> findAll(){
        return StreamSupport.stream(vehiculoRepository.findAll().spliterator(),false)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public void delete(Long id){
        vehiculoRepository.deleteById(id);
    }


    public VehiculoDTO convertToDTO(Vehiculo vehiculo){
        return VehiculoDTO.builder()
                .id(vehiculo.getId())
                .modelo(vehiculo.getModelo())
                .marca(vehiculo.getMarca())
                .placa(vehiculo.getPlaca())
                .color(vehiculo.getColor())
                .categoriaId(vehiculo.getCategoria().getId())
                .usuarioId(vehiculo.getUsuario().getId())
                .build();
    }
}
