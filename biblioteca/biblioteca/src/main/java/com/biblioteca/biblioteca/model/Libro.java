package com.biblioteca.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {
    private int id;
    private String isbn; // Código ISBN del libro
    private String titulo; // Título del libro
    private String editorial; // Editorial que publicó el libro
    private int anioPublicacion; // Año en que se publicó
    private String autor; // Autor del libro
}
