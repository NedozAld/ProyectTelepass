package com.peaje.telepass.Services.Pagos;


import com.peaje.telepass.Models.DTOs.PagoDTO;
import com.peaje.telepass.Models.DTOs.PagoLisDTO;
import com.peaje.telepass.Models.DTOs.TelepassDTO;
import com.peaje.telepass.Models.Entity.*;
import com.peaje.telepass.Models.Repository.*;
import com.peaje.telepass.Services.Email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class PagoServicio {

    private final PagoRepository pagoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final TelepassRepository telepassRepository;
    private  final ZonaRepository zonaRepository;
    private final TarifaRepository tarifaRepository;
    private final FacturaRepository facturaRepository;
    private final EmailService emailService;

    public PagoDTO realizarPago(Long vehiculoId, Long zonaId) {
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

        Telepass telepass = telepassRepository.findByVehiculoId(vehiculoId)
                .orElseThrow(() -> new RuntimeException("Telepass no encontrado para el vehículo"));

        Zona zona = zonaRepository.findById(zonaId)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));

        VehiculoCategoria categoria = vehiculo.getCategoria();

        Tarifa tarifa = tarifaRepository.findByVehiculoAndZona(categoria, zona)
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada"));

        Double monto = tarifa.getMonto();

        if (telepass.getSaldo() < monto) {
            throw new RuntimeException("Saldo insuficiente");
        }

        // Descontar saldo
        telepass.setSaldo(telepass.getSaldo() - monto);
        telepassRepository.save(telepass);

        // Registrar el pago
        Pago pago = Pago.builder()
                .zona(zona)
                .vehiculo(vehiculo)
                .usuario(telepass.getUsuario())
                .monto(monto)
                .fechaPago(LocalDate.now())
                .build();
        pagoRepository.save(pago);
        // Generar factura
        Factura factura = Factura.builder()
                .pago(pago)
                .detalle("Pago de peaje de " + monto + " en la zona " + zona.getNombre())
                .usuario(telepass.getUsuario())
                .fechaFactura(LocalDate.now())
                .build();
        facturaRepository.save(factura);

        // Envía la factura por correo electrónico
        enviarFacturaPorCorreo(factura, telepass.getUsuario().getCorreo());

        return convertToDto(pago);
    }

    private void enviarFacturaPorCorreo(Factura factura, String emailUsuario) {
        Usuario usuario = factura.getPago().getUsuario();
        Vehiculo vehiculo = factura.getPago().getVehiculo();
        Zona zona = factura.getPago().getZona();

        String subject = "Factura de Pago de Peaje";
        String body = "Estimado(a) " + usuario.getNombre() + " " + usuario.getApellido() + ",\n\n" +
                "Gracias por utilizar nuestro servicio de peaje automatizado. Aquí están los detalles de su factura:\n\n" +
                "----------------------------------------\n" +
                "INFORMACIÓN DEL USUARIO\n" +
                "Cliente: " + usuario.getNombre() + " " + usuario.getApellido() + "\n" +
                "Correo Electrónico: " + usuario.getCorreo() + "\n\n" +
                "----------------------------------------\n" +
                "DETALLES DE LA FACTURA\n" +
                "Monto: $" + factura.getPago().getMonto() + "\n" +
                "Fecha: " + factura.getFechaFactura() + "\n" +
                "Vehículo: " + vehiculo.getMarca() + " " + vehiculo.getModelo() + " (Placa: " + vehiculo.getPlaca() + ")\n" +
                "Zona: " + zona.getNombre() + "\n" +
                "----------------------------------------\n\n" +
                "Si tiene alguna pregunta o necesita asistencia, no dude en contactarnos.\n\n" +
                "Saludos cordiales,\n" +
                "El equipo de Telepass";

        emailService.sendEmail(emailUsuario, subject, body);
    }

    public PagoDTO realizarPagoEfectivo(String placa, Long zonaId) {
        // Buscar vehículo por placa
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con la placa: " + placa));

        // Buscar zona por ID
        Zona zona = zonaRepository.findById(zonaId)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada con ID: " + zonaId));

        // Obtener la categoría del vehículo
        VehiculoCategoria categoria = vehiculo.getCategoria();

        // Buscar tarifa por categoría de vehículo y zona
        Tarifa tarifa = tarifaRepository.findByVehiculoAndZona(categoria, zona)
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada para la categoría de vehículo y zona"));

        Double monto = tarifa.getMonto();




        // Registrar el pago
        Pago pago = Pago.builder()
                .zona(zona)
                .vehiculo(vehiculo)
                .usuario(vehiculo.getUsuario())
                .monto(monto)
                .fechaPago(LocalDate.now())
                .build();
        pagoRepository.save(pago);

        // Generar factura
        Factura factura = Factura.builder()
                .pago(pago)
                .detalle("Pago de peaje de " + monto + " en la zona " + zona.getNombre())
                .usuario(vehiculo.getUsuario())
                .fechaFactura(LocalDate.now())
                .build();
        facturaRepository.save(factura);

        System.out.println(vehiculo.getUsuario().getCorreo());

        // Envía la factura por correo electrónico
        enviarFacturaPorCorreo(factura, vehiculo.getUsuario().getCorreo());

        return convertToDto(pago);
    }


    public List<PagoLisDTO> findAll(){
        return StreamSupport.stream(pagoRepository.findAll().spliterator(), false)
                .map(this::convertToDtoList)
                .sorted(Comparator.comparing(PagoLisDTO::getId).reversed())
                .collect(Collectors.toList());
    }

    public List<PagoLisDTO> findByUsuarioId(Long usuarioId) {
        List<Pago> pagos = pagoRepository.findByUsuarioId(usuarioId);
        return pagos.stream()
                .map(this::convertToDtoList)
                .sorted(Comparator.comparing(PagoLisDTO::getId).reversed())
                .collect(Collectors.toList());
    }

    public Double ObtenerTarifa(String placa, Long zonaId){
        // Buscar vehículo por placa
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con la placa: " + placa));

        // Buscar zona por ID
        Zona zona = zonaRepository.findById(zonaId)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada con ID: " + zonaId));

        // Obtener la categoría del vehículo
        VehiculoCategoria categoria = vehiculo.getCategoria();

        // Buscar tarifa por categoría de vehículo y zona
        Tarifa tarifa = tarifaRepository.findByVehiculoAndZona(categoria, zona)
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada para la categoría de vehículo y zona"));

        return tarifa.getMonto();
    }

    public Double ObtenerTarifaPago(Long vehiculoId, Long zonaId){

        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

        Telepass telepass = telepassRepository.findByVehiculoId(vehiculoId)
                .orElseThrow(() -> new RuntimeException("Telepass no encontrado para el vehículo"));

        Zona zona = zonaRepository.findById(zonaId)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
        // Obtener la categoría del vehículo
        VehiculoCategoria categoria = vehiculo.getCategoria();

        // Buscar tarifa por categoría de vehículo y zona
        Tarifa tarifa = tarifaRepository.findByVehiculoAndZona(categoria, zona)
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada para la categoría de vehículo y zona"));

        return tarifa.getMonto();
    }




    public PagoDTO convertToDto(Pago pago){
        return PagoDTO.builder()
                .id(pago.getId())
                .zonaId(pago.getZona().getId())
                .vehiculoId(pago.getVehiculo().getId())
                .usuarioId(pago.getUsuario().getId())
                .monto(pago.getMonto())
                .fechaPago(pago.getFechaPago())
                .build();
    }

    public PagoLisDTO convertToDtoList(Pago pago){
        return PagoLisDTO.builder()
                .id(pago.getId())
                .monto(pago.getMonto())
                .zona(pago.getZona().getNombre())
                .vehiculo(pago.getVehiculo().getMarca()+" "+pago.getVehiculo().getModelo())
                .usuario(pago.getUsuario().getNombre()+" "+pago.getUsuario().getApellido())
                .fechaPago(pago.getFechaPago())
                .build();
    }
}
