package com.perfulandia.saleservice.model;

import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long productoId; //Solo guarda Id del producto
    private int cantidad;
    private double precioUnitario;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    @JsonBackReference //Se agrega para evitar loop infinito en POST al visualizar detalle
    private Venta venta;
}
