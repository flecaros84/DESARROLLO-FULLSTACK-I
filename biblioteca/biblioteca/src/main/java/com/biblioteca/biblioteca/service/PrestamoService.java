package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.model.Prestamo;
import com.biblioteca.biblioteca.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service

public class PrestamoService {

    //
    @Autowired //Permite inyectar
    private PrestamoRepository repositorioInyectado;

    //Agregar
    public void crearPrestamo(Prestamo prestamo) {
        repositorioInyectado.agregarPrestamo(prestamo);
    }

    //Listar
    public List<Prestamo> listarPrestamos() {
        return repositorioInyectado.obtenerTodosLosPrestamos();
    }


}
