package com.peaje.telepass.Models.Repository;

import com.peaje.telepass.Models.Entity.Vehiculo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends CrudRepository<Vehiculo,Long> {
}
