package com.perfulandia.inventoryservice.repository;

import com.perfulandia.inventoryservice.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    List<Sucursal> findAll();
}