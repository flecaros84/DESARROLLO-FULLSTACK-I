package com.perfulandia.saleservice.service;

import com.perfulandia.saleservice.model.Venta;
import com.perfulandia.saleservice.model.DetalleVenta;
import com.perfulandia.saleservice.repository.VentaRepository;
import org.springframework.stereotype.Service;

import com.perfulandia.saleservice.repository.FacturaRepository;
import com.perfulandia.saleservice.model.Factura;
import java.util.UUID;

import java.util.List;
import java.time.LocalDate;

@Service
public class VentaService {

    public final VentaRepository ventaRepository;
    private final FacturaRepository facturaRepository;

    public VentaService(VentaRepository ventaRepository, FacturaRepository facturaRepository) {
        this.ventaRepository = ventaRepository;
        this.facturaRepository = facturaRepository;
    }

    //listar
    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    //guardar
    public Venta guardar(Venta venta) {
        venta.setFecha(LocalDate.now());
        double total = 0.0;
        if (venta.getDetalles() != null) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                detalle.setVenta(venta);

                double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
                total += subtotal;
            }
        }
        venta.setTotal(Math.round(total * 100.0) / 100.0);

        Venta ventaGuardada = ventaRepository.save(venta);

        // Cálculo de IVA desde el total bruto
        double neto = Math.round((total / 1.19) * 100.0) / 100.0;
        double iva = Math.round((total - neto) * 100.0) / 100.0;

        // Crear la factura con desglose
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

    //buscar
    public Venta buscar(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    //eliminar
    public void eliminar(Long id) {
        ventaRepository.deleteById(id);
    }

}
