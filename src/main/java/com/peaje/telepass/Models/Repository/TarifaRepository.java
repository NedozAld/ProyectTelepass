package com.peaje.telepass.Models.Repository;

import com.peaje.telepass.Models.Entity.Tarifa;
import com.peaje.telepass.Models.Entity.VehiculoCategoria;
import com.peaje.telepass.Models.Entity.Zona;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarifaRepository extends CrudRepository<Tarifa,Long> {

   Optional<Tarifa>findByVehiculoAndZona(VehiculoCategoria vehiculo, Zona zona);
}
