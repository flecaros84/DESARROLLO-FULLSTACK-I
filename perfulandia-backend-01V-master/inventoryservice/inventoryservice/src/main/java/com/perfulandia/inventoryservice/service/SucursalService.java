package com.perfulandia.inventoryservice.service;

import com.perfulandia.inventoryservice.model.Sucursal;
import com.perfulandia.inventoryservice.repository.SucursalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService {

    private final SucursalRepository sucursalRepository;

    public SucursalService(SucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    public List<Sucursal> listar() {
        return sucursalRepository.findAll();
    }

    public Sucursal guardar(Sucursal sucursal) {
        return sucursalRepository.save(sucursal);
    }

    public Sucursal buscar(long id) {
        return sucursalRepository.findById(id).orElse(null);
    }

    public void eliminar(long id) {
        sucursalRepository.deleteById(id);
    }
}