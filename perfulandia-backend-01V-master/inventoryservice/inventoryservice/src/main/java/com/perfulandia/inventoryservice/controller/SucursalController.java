package com.perfulandia.inventoryservice.controller;

import com.perfulandia.inventoryservice.model.Sucursal;
import com.perfulandia.inventoryservice.service.SucursalService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sucursales")
public class SucursalController {

    private final SucursalService sucursalService;

    public SucursalController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @GetMapping
    public List<Sucursal> listar() {
        return sucursalService.listar();
    }

    @PostMapping
    public Sucursal guardar(@RequestBody Sucursal sucursal) {
        return sucursalService.guardar(sucursal);
    }

    @GetMapping("/{id}")
    public Sucursal buscar(@PathVariable long id) {
        return sucursalService.buscar(id);
    }

    @PutMapping("/{id}")
    public Sucursal actualizar(@PathVariable long id, @RequestBody Sucursal sucursalActualizada) {
        Sucursal existente = sucursalService.buscar(id);
        if (existente != null) {
            sucursalActualizada.setId(id);
            return sucursalService.guardar(sucursalActualizada);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable long id) {
        sucursalService.eliminar(id);
    }
}