package com.peaje.telepass.Services.Telepass;

import com.peaje.telepass.Models.DTOs.TelepassDTO;
import com.peaje.telepass.Models.Entity.Telepass;
import com.peaje.telepass.Models.Entity.TipoPago;
import com.peaje.telepass.Models.Entity.Usuario;
import com.peaje.telepass.Models.Entity.Vehiculo;
import com.peaje.telepass.Models.Repository.TelepassRepository;
import com.peaje.telepass.Models.Repository.TipoPagoRepository;
import com.peaje.telepass.Models.Repository.UsuarioRepository;
import com.peaje.telepass.Models.Repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelepassService {
    private  final TelepassRepository telepassRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoPagoRepository tipoPagoRepository;
    private final VehiculoRepository vehiculoRepository;

    public TelepassDTO save(TelepassDTO telepassDTO) {
        Usuario usuario = usuarioRepository.findById(telepassDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        TipoPago tipoPago = tipoPagoRepository.findById(telepassDTO.getTipoPagoId())
                .orElseThrow(() -> new RuntimeException("Tipo de pago no encontrado"));

        // Verificar si el vehículo ya tiene un Telepass asociado
        if (telepassRepository.existsByVehiculoId(telepassDTO.getVehiculoId())) {
            throw new RuntimeException("El vehículo ya tiene un Telepass asociado.");
        }

        Vehiculo vehiculo = vehiculoRepository.findById(telepassDTO.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        Telepass telepass = Telepass.builder()
                .usuario(usuario)
                .saldo(telepassDTO.getSaldo())
                .activo(true)
                .tipoPago(tipoPago)
                .vehiculo(vehiculo)
                .build();

        return convertToDto(telepassRepository.save(telepass));
    }



    public TelepassDTO recargar(Long id, Double montoRecarga) {
        Telepass telepass = telepassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Telepass no encontrado"));

        Double saldoActual = telepass.getSaldo();
        Double saldoNuevo = saldoActual + montoRecarga;
        telepass.setSaldo(saldoNuevo);

        return convertToDto(telepassRepository.save(telepass));
    }

    public TelepassDTO cambiarTitularidad(Long telepassId, Long nuevoUsuarioId) {
        Telepass telepass = telepassRepository.findById(telepassId)
                .orElseThrow(() -> new RuntimeException("Telepass no encontrado"));

        Usuario nuevoUsuario = usuarioRepository.findById(nuevoUsuarioId)
                .orElseThrow(() -> new RuntimeException("Nuevo usuario no encontrado"));

        if(telepass != null && nuevoUsuario != null){
            telepass.setUsuario(nuevoUsuario);
            return convertToDto(telepassRepository.save(telepass));
        }else{
            return null;
        }


    }

    public List<TelepassDTO> findByUsuarioId(Long usuarioId) {
        List<Telepass> telepasses = telepassRepository.findByUsuarioId(usuarioId);
        return telepasses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



    public TelepassDTO convertToDto(Telepass telepass){
        return TelepassDTO.builder()
                .id(telepass.getId())
                .usuarioId(telepass.getUsuario().getId())
                .saldo(telepass.getSaldo())
                .activo(telepass.getActivo())
                .tipoPagoId(telepass.getTipoPago().getId())
                .vehiculoId(telepass.getVehiculo().getId())
                .build();
    }
}
