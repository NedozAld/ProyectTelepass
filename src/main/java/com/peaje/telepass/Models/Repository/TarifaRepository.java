package com.peaje.telepass.Models.Repository;

import com.peaje.telepass.Models.Entity.Tarifa;
import com.peaje.telepass.Models.Entity.VehiculoCategoria;
import com.peaje.telepass.Models.Entity.Zona;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TarifaRepository extends CrudRepository<Tarifa,Long> {
   List<Tarifa> findByVehiculoAndZona(VehiculoCategoria vehiculo, Zona zona);
}
