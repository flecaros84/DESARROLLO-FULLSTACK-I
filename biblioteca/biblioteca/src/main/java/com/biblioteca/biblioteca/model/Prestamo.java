package com.biblioteca.biblioteca.model;

//import
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//import de Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Prestamo {
    //Atributos
    private int id_prestamo;
    private int id_libro;
    private String run_solicitante;
    private Date fecha_solicitud;
    private Date fecha_entrega;
    private int cantidad_dias;
    private int multas;
}

