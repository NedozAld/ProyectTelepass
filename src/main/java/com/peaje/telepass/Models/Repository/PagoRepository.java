package com.peaje.telepass.Models.Repository;
// clases
import com.peaje.telepass.Models.Entity.Pago;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PagoRepository extends CrudRepository<Pago,Long> {
    List<Pago> findByUsuarioId(Long usuarioId);
}
