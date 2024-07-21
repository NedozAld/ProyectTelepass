package com.peaje.telepass.Models.Repository;
// clases
import com.peaje.telepass.Models.Entity.Telepass;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TelepassRepository extends CrudRepository<Telepass, Long> {

    List<Telepass> findByUsuarioId(Long usuarioId);
    boolean existsByVehiculoId(Long vehiculoId);
    Optional<Telepass> findByVehiculoId(Long vehiculoId);
}
