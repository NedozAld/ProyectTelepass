package com.peaje.telepass.Services.Telepass;

import com.peaje.telepass.Models.DTOs.TarifaListDTO;
import com.peaje.telepass.Models.DTOs.TelepassDTO;
import com.peaje.telepass.Models.DTOs.TelepassLisDTO;
import com.peaje.telepass.Models.Entity.Telepass;
import com.peaje.telepass.Models.Entity.TipoPago;
import com.peaje.telepass.Models.Entity.Usuario;
import com.peaje.telepass.Models.Entity.Vehiculo;
import com.peaje.telepass.Models.Repository.TelepassRepository;
import com.peaje.telepass.Models.Repository.TipoPagoRepository;
import com.peaje.telepass.Models.Repository.UsuarioRepository;
import com.peaje.telepass.Models.Repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

        TipoPago tipoPago = null;
        if (telepassDTO.getTipoPagoId() != null) {
            tipoPago = tipoPagoRepository.findById(telepassDTO.getTipoPagoId())
                    .orElseThrow(() -> new RuntimeException("Tipo de pago no encontrado"));
        }

        // Verificar si el vehículo ya tiene un Telepass asociado
        if (telepassRepository.existsByVehiculoId(telepassDTO.getVehiculoId())) {
            throw new RuntimeException("El vehículo ya tiene un Telepass asociado.");
        }

        Vehiculo vehiculo = vehiculoRepository.findById(telepassDTO.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        Telepass telepass = Telepass.builder()
                .usuario(usuario)
                .saldo(0.0)
                .activo(false)
                .nombre("T-"+vehiculo.getPlaca())
                .tipoPago(tipoPago) // Aquí se asigna null si no se encontró o no se proporcionó tipoPagoId
                .vehiculo(vehiculo)
                .build();

        return convertToDto(telepassRepository.save(telepass));
    }




    public TelepassDTO recargar(Long id, Double montoRecarga, Long idPago) {
        Telepass telepass = telepassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Telepass no encontrado"));

        if (!telepass.getActivo()) {
            throw new RuntimeException("El Telepass no está activo");
        }

        TipoPago tipoPago = tipoPagoRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Tipo de pago no encontrado"));

        Double saldoActual = telepass.getSaldo();
        Double saldoNuevo = saldoActual + montoRecarga;
        telepass.setSaldo(saldoNuevo);
        telepass.setTipoPago(tipoPago);

        telepassRepository.save(telepass);

        return convertToDto(telepass);
    }


    public TelepassDTO aprobarTelepass(Long id){
        Telepass telepass = telepassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Telepass no encontrado"));

        telepass.setActivo(true);
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

    public List<TelepassLisDTO> findByUsuarioId(Long usuarioId) {
        List<Telepass> telepasses = telepassRepository.findByUsuarioId(usuarioId);
        return telepasses.stream()
                .map(this::convertToDtoList)
                .sorted(Comparator.comparing(TelepassLisDTO::getId).reversed())
                .collect(Collectors.toList());
    }

    public List<TelepassLisDTO> findByUsuarioIdTrue(Long usuarioId) {
        List<Telepass> telepasses = telepassRepository.findByUsuarioId(usuarioId);
        return telepasses.stream()
                .filter(telepass -> Boolean.TRUE.equals(telepass.getActivo())) // Usar el getter correspondiente
                .map(this::convertToDtoList)
                .sorted(Comparator.comparing(TelepassLisDTO::getId).reversed())
                .collect(Collectors.toList());
    }

    public List<TelepassLisDTO> findAll(){
        return StreamSupport.stream(telepassRepository.findAll().spliterator(),false)
                .map(this::convertToDtoList)
                .sorted(Comparator.comparing(TelepassLisDTO::getId).reversed())
                .collect(Collectors.toList());
    }





    public TelepassDTO convertToDto(Telepass telepass) {
        return TelepassDTO.builder()
                .id(telepass.getId())
                .usuarioId(telepass.getUsuario().getId())
                .saldo(telepass.getSaldo())
                .activo(telepass.getActivo())
                .nombre(telepass.getNombre())
                .tipoPagoId(telepass.getTipoPago() != null ? telepass.getTipoPago().getId() : null)
                .vehiculoId(telepass.getVehiculo().getId())
                .build();
    }
    public TelepassLisDTO convertToDtoList(Telepass telepass) {
        return TelepassLisDTO.builder()
                .id(telepass.getId())
                .usuarioId(telepass.getUsuario().getNombre() + " " +telepass.getUsuario().getApellido())
                .saldo(telepass.getSaldo())
                .activo(telepass.getActivo())
                .nombre(telepass.getNombre())
                .tipoPagoId(telepass.getTipoPago() != null ? telepass.getTipoPago().getId() : null)
                .vehiculoId(telepass.getVehiculo().getMarca() + " "+ telepass.getVehiculo().getModelo())
                .build();
    }

}
