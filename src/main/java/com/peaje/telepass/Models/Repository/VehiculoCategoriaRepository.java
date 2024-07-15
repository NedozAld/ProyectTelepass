package com.peaje.telepass.Models.Repository;

import com.peaje.telepass.Models.Entity.VehiculoCategoria;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VehiculoCategoriaRepository extends CrudRepository<VehiculoCategoria,Long> {

   // Optional<Tarifa> findByVehiculoAndZona(VehiculoCategoria vehiculoCategoria, Zona zona);
}
