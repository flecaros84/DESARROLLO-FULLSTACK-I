package com.perfulandia.saleservice.dto;
import lombok.*;
import com.perfulandia.saleservice.model.Venta;
import com.perfulandia.saleservice.model.Factura;
import com.perfulandia.saleservice.model.Usuario;

import java.util.List;
import java.time.LocalDate;

@Data
@Builder
public class BoletaCompletaDTO {
    private Long ventaId;
    private LocalDate fecha;
    private String medioPago;
    private double total;

    private Factura factura;
    private Usuario cliente;
    private List<DetalleBoletaDTO> detalles;
}