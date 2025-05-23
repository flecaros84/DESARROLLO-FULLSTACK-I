package com.perfulandia.inventoryservice.repository;

import com.perfulandia.inventoryservice.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Optional<Inventario> findByProductoIdAndSucursalId(Long productoId, Long sucursalId);
    List<Inventario> findBySucursalId(Long sucursalId);
    List<Inventario> findByProductoId(Long productoId);
}