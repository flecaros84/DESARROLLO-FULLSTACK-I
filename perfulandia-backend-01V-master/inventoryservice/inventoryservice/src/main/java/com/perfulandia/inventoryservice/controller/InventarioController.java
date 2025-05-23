package com.perfulandia.inventoryservice.controller;

import com.perfulandia.inventoryservice.model.Inventario;
import com.perfulandia.inventoryservice.service.InventarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/todo")
    public List<Inventario> listarTodoElInventario() {
        return inventarioService.listarTodoElInventario();
    }

    @GetMapping("/stock")
    public Inventario getStockByProductoAndSucursal(
            @RequestParam long productoId,
            @RequestParam long sucursalId) {
        return inventarioService.buscarStockPorProductoYSucursal(productoId, sucursalId);
    }

    @GetMapping("/sucursal/{sucursalId}")
    public List<Inventario> getStockBySucursal(@PathVariable long sucursalId) {
        return inventarioService.listarStockPorSucursal(sucursalId);
    }

    @GetMapping("/producto/{productoId}")
    public List<Inventario> getStockByProducto(@PathVariable long productoId) {
        return inventarioService.listarStockPorProducto(productoId);
    }

    // Para crear/actualizar una entrada de inventario (establecer cantidad)
    @PostMapping("/stock")
    public Inventario guardarOActualizarStock(@RequestBody Inventario inventario) {
        return inventarioService.guardarOActualizarStock(inventario);
    }

    // Para ajustar el stock (incrementar o decrementar)
    @PatchMapping("/stock/ajustar")
    public Inventario ajustarStock(@RequestBody Map<String, Object> payload) {
        try {
            long productoId = Long.parseLong(payload.get("productoId").toString());
            long sucursalId = Long.parseLong(payload.get("sucursalId").toString());
            int cantidadAjuste = Integer.parseInt(payload.get("cantidadAjuste").toString());
            return inventarioService.ajustarStock(productoId, sucursalId, cantidadAjuste);
        } catch (Exception e) {
            System.err.println("Error al ajustar stock: " + e.getMessage());
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void eliminarEntradaInventario(@PathVariable long id) {
        inventarioService.eliminarEntradaInventario(id);
    }
}