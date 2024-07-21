package com.peaje.telepass.Models.Repository;

// clases
import com.peaje.telepass.Models.DTOs.VehiculoListDTO;
import com.peaje.telepass.Models.Entity.Vehiculo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends CrudRepository<Vehiculo,Long> {
    boolean existsByPlaca(String placa);

    List<Vehiculo> findByUsuarioId(Long usuarioId);


    Optional<Vehiculo> findByPlaca(String placa);
}
