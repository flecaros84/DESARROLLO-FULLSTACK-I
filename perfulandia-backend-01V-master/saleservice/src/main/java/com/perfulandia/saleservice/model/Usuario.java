package com.perfulandia.saleservice.model;


import lombok.*;

//Esta clase existe para hacer las consultas por el REST al usuarioservice
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private long id;
    private String nombre, correo, rol;

}
