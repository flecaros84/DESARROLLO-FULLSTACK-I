package com.perfulandia.productservice.model;

import lombok.*;

//DIO Data Transfer Object
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private long id;
    private String nombre,correo,rol;
}
