package com.peaje.telepass.Models.Repository;

import com.peaje.telepass.Models.Entity.Zona;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaRepository extends CrudRepository<Zona,Long> {
}
