package com.biblioteca.biblioteca.repository;

//import
import com.biblioteca.biblioteca.model.Prestamo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;

@Repository //Indicamos que esta clase se comportará como un repositorio

public class PrestamoRepository {
    private List<Prestamo> prestamos = new ArrayList<>();

    //Método para poder agregar prestamos a mi lista
    public void agregarPrestamo(Prestamo prestamo) {
        prestamos.add(prestamo);
    }

    //Método para poder listar todos los prestamos
    public List<Prestamo> obtenerTodosLosPrestamos() {
        return prestamos;
    }



}
