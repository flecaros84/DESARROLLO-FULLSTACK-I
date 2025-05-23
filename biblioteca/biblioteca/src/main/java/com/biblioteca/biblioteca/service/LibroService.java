package com.biblioteca.biblioteca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.biblioteca.biblioteca.repository.LibroRepository;
import com.biblioteca.biblioteca.model.Libro;
import java.util.List;
import java.util.Optional;
@Service
public class LibroService {
    @Autowired
    private LibroRepository libroRepository; // Inyección del repositorio

    public int obtenerTotalLibros() {
        return libroRepository.totalLibros(); // Solicita el total al repositorio
    }

    public Optional<Libro> buscarLibroPorIsbn(String isbn) {
        return libroRepository.buscarPorIsbn(isbn);
    }

    public long contarLibrosPorAnio(int anio) {
        return libroRepository.contarPorAnio(anio);
    }

    public List<Libro> buscarLibrosPorAutor(String autor) {
        return libroRepository.buscarPorAutor(autor);
    }

    public Optional<Libro> obtenerLibroMasAntiguo() {
        return libroRepository.libroMasAntiguo();
    }

    public Optional<Libro> obtenerLibroMasNuevo() {
        return libroRepository.libroMasNuevo();
    }

    public List<Libro> listarLibrosOrdenadosPorAnio() {
        return libroRepository.listarOrdenadosPorAnio();
    }
}
