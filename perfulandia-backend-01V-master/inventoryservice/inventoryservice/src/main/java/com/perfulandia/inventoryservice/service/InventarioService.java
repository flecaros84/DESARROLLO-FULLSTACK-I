package com.perfulandia.inventoryservice.service;

import com.perfulandia.inventoryservice.model.Inventario;
import com.perfulandia.inventoryservice.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public List<Inventario> listarTodoElInventario() {
        return inventarioRepository.findAll();
    }

    public Inventario buscarInventarioPorId(long id) {
        return inventarioRepository.findById(id).orElse(null);
    }

    public Inventario buscarStockPorProductoYSucursal(long productoId, long sucursalId) {
        return inventarioRepository.findByProductoIdAndSucursalId(productoId, sucursalId).orElse(null);
    }

    public List<Inventario> listarStockPorSucursal(long sucursalId) {
        return inventarioRepository.findBySucursalId(sucursalId);
    }

    public List<Inventario> listarStockPorProducto(long productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }

    @Transactional
    public Inventario guardarOActualizarStock(Inventario inventarioInput) {
        Optional<Inventario> existenteOpt = inventarioRepository.findByProductoIdAndSucursalId(
                inventarioInput.getProductoId(),
                inventarioInput.getSucursalId()
        );

        if (existenteOpt.isPresent()) {
            Inventario existente = existenteOpt.get();
            existente.setCantidad(inventarioInput.getCantidad());
            return inventarioRepository.save(existente);
        } else {
            return inventarioRepository.save(inventarioInput);
        }
    }

    @Transactional
    public Inventario ajustarStock(long productoId, long sucursalId, int cantidadAjuste) {
        Inventario inventario = inventarioRepository.findByProductoIdAndSucursalId(productoId, sucursalId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado")); // Simplificado

        int nuevaCantidad = inventario.getCantidad() + cantidadAjuste;
        if (nuevaCantidad < 0) {
            throw new RuntimeException("Stock no puede ser negativo"); // Simplificado
        }
        inventario.setCantidad(nuevaCantidad);
        return inventarioRepository.save(inventario);
    }

    public void eliminarEntradaInventario(long id) {
        inventarioRepository.deleteById(id);
    }
}