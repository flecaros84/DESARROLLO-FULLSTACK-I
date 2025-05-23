package com.perfulandia.saleservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo; // Ej: "BOLETA" o "FACTURA"
    private String folio; // Puedes usar UUID o número correlativo
    private LocalDate fechaEmision;

    @OneToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    //Campos para el IVA
    private double neto;
    private double iva;
    private double totalConIva;

}
