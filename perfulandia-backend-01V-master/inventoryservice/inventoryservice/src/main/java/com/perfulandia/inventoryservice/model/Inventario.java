package com.perfulandia.inventoryservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // ID de la entrada de inventario
    private long productoId; // ID del producto (referencia a ProductService)
    private long sucursalId;
    private int cantidad;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}