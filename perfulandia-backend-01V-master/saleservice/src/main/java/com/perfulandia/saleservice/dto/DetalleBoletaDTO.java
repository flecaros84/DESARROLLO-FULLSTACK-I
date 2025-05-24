package com.perfulandia.saleservice.dto;

import lombok.*;

@Data
@Builder
public class DetalleBoletaDTO {
    private long productoId;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
}
