package com.perfulandia.saleservice.service;

import com.perfulandia.saleservice.model.Venta;
import com.perfulandia.saleservice.model.DetalleVenta;
import com.perfulandia.saleservice.model.Factura;
import com.perfulandia.saleservice.repository.VentaRepository;
import com.perfulandia.saleservice.repository.FacturaRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final FacturaRepository facturaRepository;
    private final RestTemplate restTemplate;

    public VentaService(VentaRepository ventaRepository, FacturaRepository facturaRepository, RestTemplate restTemplate) {
        this.ventaRepository = ventaRepository;
        this.facturaRepository = facturaRepository;
        this.restTemplate = restTemplate;
    }

    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    public Venta guardar(Venta venta) {
        venta.setFecha(LocalDate.now());
        double total = 0.0;

        if (venta.getDetalles() != null) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                // 1. Obtener producto desde productservice
                // 2. Obtener producto y verificar stock (LÓGICA SIMPLIFICADA TEMPORALMENTE)
                // String productoUrl = "http://localhost:8082/api/productos/" + detalle.getProductoId();
                // Producto producto = restTemplate.getForObject(productoUrl, Producto.class);
                // if (producto == null) {
                //     throw new RuntimeException("Producto no encontrado con ID: " + detalle.getProductoId());
                // }
                // if (producto.getStock() < detalle.getCantidad()) {
                //     throw new RuntimeException("Stock insuficiente para producto ID: " + detalle.getProductoId());
                // }

                // 3. Asignar precio unitario y venta (PRECIO FIJO TEMPORAL)
                // detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setPrecioUnitario(1.0); // Precio fijo temporal para pruebas
                detalle.setVenta(venta);

                // 4. Ajustar stock en inventoryservice
                String inventoryServiceUrl = "http://localhost:8083/api/v1/inventario/stock/ajustar";
                Map<String, Object> inventoryAdjustmentBody = new HashMap<>();
                inventoryAdjustmentBody.put("productoId", detalle.getProductoId());
                inventoryAdjustmentBody.put("sucursalId", 1L); // ID de sucursal fijo por ahora
                inventoryAdjustmentBody.put("cantidadAjuste", -detalle.getCantidad()); // Negativo para descontar

                HttpHeaders inventoryHeaders = new HttpHeaders();
                inventoryHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Map<String, Object>> inventoryEntity = new HttpEntity<>(inventoryAdjustmentBody, inventoryHeaders);

                try {
                    ResponseEntity<Void> inventoryResponse = restTemplate.exchange(
                            inventoryServiceUrl,
                            HttpMethod.PATCH, // Usar PATCH para el endpoint de ajuste de inventario
                            inventoryEntity,
                            Void.class
                    );
                    // Opcional: Verificar el código de estado de la respuesta
                    if (!inventoryResponse.getStatusCode().is2xxSuccessful()) {
                        throw new RuntimeException("Error al ajustar stock en inventoryservice para producto ID: " + detalle.getProductoId() +
                                ".Estado: " + inventoryResponse.getStatusCode().value());
                    }
                } catch (Exception e) {
                    System.err.println("Error llamando a inventoryservice para ajustar stock del producto ID: " + detalle.getProductoId() + ". Mensaje: " + e.getMessage());
                    e.printStackTrace(); // Añadido para traza completa
                    throw new RuntimeException("Fallo al comunicar con inventoryservice para ajustar stock del producto ID: " + detalle.getProductoId(), e);
                }

                // 5. Acumular total
                total += detalle.getCantidad() * detalle.getPrecioUnitario();
            }
        }

        // Redondear total
        total = Math.round(total * 100.0) / 100.0;
        venta.setTotal(total);

        Venta ventaGuardada = ventaRepository.save(venta);

        // Crear factura con desglose de IVA
        double neto = Math.round((total / 1.19) * 100.0) / 100.0;
        double iva = Math.round((total - neto) * 100.0) / 100.0;

        Factura factura = Factura.builder()
                .tipo("BOLETA")
                .folio(UUID.randomUUID().toString())
                .fechaEmision(LocalDate.now())
                .venta(ventaGuardada)
                .neto(neto)
                .iva(iva)
                .totalConIva(total)
                .build();

        facturaRepository.save(factura);

        return ventaGuardada;
    }

    public Venta buscar(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        ventaRepository.deleteById(id);
    }

    // (COMENTADO TEMPORALMENTE)
    // public BoletaCompletaDTO obtenerBoletaCompleta(Long ventaId) {
    //     Venta venta = ventaRepository.findById(ventaId)
    //             .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + ventaId));

    //     Usuario usuario = restTemplate.getForObject("http://localhost:8081/api/usuarios/" + venta.getClienteId(), Usuario.class);

    //     List<ProductoDTO> productosDTO = venta.getDetalles().stream().map(detalle -> {
    //         Producto producto = restTemplate.getForObject("http://localhost:8082/api/productos/" + detalle.getProductoId(), Producto.class);
    //         return new ProductoDTO(producto.getNombre(), producto.getDescripcion(), detalle.getCantidad(), detalle.getPrecioUnitario());
    //     }).collect(Collectors.toList());

    //     return new BoletaCompletaDTO(venta, usuario, productosDTO);
    // }
}
