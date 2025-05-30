package com.perfulandia.saleservice.model;

import lombok.*;

//Esta clase existe para hacer las consultas por el REST al productoservice

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    private long id;
    private String nombre;
    private double precio;
    private int stock;
}
