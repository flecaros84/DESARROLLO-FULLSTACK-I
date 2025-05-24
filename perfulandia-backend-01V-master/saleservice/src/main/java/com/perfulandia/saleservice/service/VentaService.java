package com.perfulandia.saleservice.service;

import com.perfulandia.saleservice.model.Venta;
import com.perfulandia.saleservice.model.DetalleVenta;
import com.perfulandia.saleservice.model.Factura;
import com.perfulandia.saleservice.model.Producto;
//Importaciones para generar boleta completa
import com.perfulandia.saleservice.model.Usuario;
import com.perfulandia.saleservice.dto.BoletaCompletaDTO;
import com.perfulandia.saleservice.dto.DetalleBoletaDTO;
//
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
                Producto producto = restTemplate.getForObject(
                        "http://localhost:8082/api/productos/" + detalle.getProductoId(),
                        Producto.class
                );

                if (producto == null) {
                    throw new RuntimeException("Producto no encontrado: ID " + detalle.getProductoId());
                }

                // 2. Verificar stock
                if (producto.getStock() < detalle.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para producto ID: " + detalle.getProductoId());
                }

                // 3. Asignar precio unitario y venta
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setVenta(venta);

                // 4. Descontar stock usando PUT
                String url = "http://localhost:8082/api/productos/" + detalle.getProductoId() + "/descontar";
                Map<String, Integer> body = Map.of("cantidad", detalle.getCantidad());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(body, headers);

                restTemplate.exchange(
                        url,
                        HttpMethod.PUT,
                        entity,
                        Void.class
                );

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

    public BoletaCompletaDTO obtenerBoletaCompleta(Long ventaId) {
        Venta venta = ventaRepository.findById(ventaId).orElseThrow();
        Factura factura = facturaRepository.findByVentaId(ventaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada para la venta " + ventaId));

        // Obtener cliente desde usuarioservice
        Usuario cliente = restTemplate.getForObject(
                "http://localhost:8081/api/usuarios/" + venta.getClienteId(), Usuario.class
        );

        // Armar los detalles
        List<DetalleBoletaDTO> detallesDTO = new ArrayList<>();
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = restTemplate.getForObject(
                    "http://localhost:8082/api/productos/" + detalle.getProductoId(), Producto.class
            );

            detallesDTO.add(DetalleBoletaDTO.builder()
                    .productoId(producto.getId())
                    .nombreProducto(producto.getNombre())
                    .cantidad(detalle.getCantidad())
                    .precioUnitario(detalle.getPrecioUnitario())
                    .subtotal(detalle.getCantidad() * detalle.getPrecioUnitario())
                    .build());
        }

        return BoletaCompletaDTO.builder()
                .ventaId(venta.getId())
                .fecha(venta.getFecha())
                .medioPago(venta.getMedioPago())
                .total(venta.getTotal())
                .factura(factura)
                .cliente(cliente)
                .detalles(detallesDTO)
                .build();
    }
}
