package com.biblioteca.biblioteca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.biblioteca.biblioteca.model.Libro;
import com.biblioteca.biblioteca.service.LibroService;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/libros")
public class LibroController {
    @Autowired
    private LibroService libroService;

    // Endpoint para obtener el total de libros
    @GetMapping("/total")
    public int totalLibros() {
        return libroService.obtenerTotalLibros();
    }

    // Buscar libro por ISBN
    @GetMapping("/buscar/isbn/{isbn}")
    public Optional<Libro> buscarPorIsbn(@PathVariable String isbn) {
        return libroService.buscarLibroPorIsbn(isbn);
    }

    // Contar libros por año
    @GetMapping("/buscar/anio/{anio}")
    public long contarPorAnio(@PathVariable int anio) {
        return libroService.contarLibrosPorAnio(anio);
    }

    // Buscar libros por autor
    @GetMapping("/buscar/autor/{autor}")
    public List<Libro> buscarPorAutor(@PathVariable String autor) {
        return libroService.buscarLibrosPorAutor(autor);
    }

    // Obtener libro más antiguo
    @GetMapping("/buscar/antiguo")
    public Optional<Libro> libroMasAntiguo() {
        return libroService.obtenerLibroMasAntiguo();
    }

    // Obtener libro más nuevo
    @GetMapping("/buscar/nuevo")
    public Optional<Libro> libroMasNuevo() {
        return libroService.obtenerLibroMasNuevo();
    }

    // Listar todos los libros ordenados por año
    @GetMapping("/listar/ordenado")
    public List<Libro> listarOrdenadosPorAnio() {
        return libroService.listarLibrosOrdenadosPorAnio();
    }
}
