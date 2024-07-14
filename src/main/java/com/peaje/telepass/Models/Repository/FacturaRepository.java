package com.peaje.telepass.Models.Repository;

import com.peaje.telepass.Models.Entity.Factura;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends CrudRepository<Factura,Long> {
}
