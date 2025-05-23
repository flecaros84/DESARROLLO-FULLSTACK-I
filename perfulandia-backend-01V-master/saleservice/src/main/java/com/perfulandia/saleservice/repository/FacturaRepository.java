package com.perfulandia.saleservice.repository;

import com.perfulandia.saleservice.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    Optional<Factura> findByVentaId(Long ventaId);
}
