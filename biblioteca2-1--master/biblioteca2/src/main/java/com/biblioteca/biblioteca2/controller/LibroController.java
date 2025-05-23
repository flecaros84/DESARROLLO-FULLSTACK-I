package com.biblioteca.biblioteca2.controller;

//import
import com.biblioteca.biblioteca2.model.Libro;
import com.biblioteca.biblioteca2.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController //Controlador REST
@RequestMapping("/libros")

public class LibroController {
    @Autowired
    private LibroService libroService;

    //5 Endpoint
    @GetMapping("/total")
    public int totalLibros() {
       return libroService.obtenerTotalLibros();
    }

    //6 ibsn
    @GetMapping("/buscar/isbn/{isbn}")
    public Optional<Libro> buscarIsbn(@PathVariable String isbn) {
        return libroService.buscarLibroPorIsbn(isbn);
    }

    @GetMapping("/buscar/anio/{anio}")
    public long contarPorAnio(@PathVariable int anio) {
        return LibroService.contarLibrosPorAnio(anio);

    }

    @GetMapping("/buscar/autor/{autor}")
    public List<Libro> buscarPorAutor(@PathVariable String autor) {
        return libroService.buscarLibrosPorAutor(autor);
    }

    @GetMapping("/buscar/antiguo")
    public Optional<Libro> libroMasAntiguo() {
        return libroService.obtenerLibroMasAntiguo();
    }

    @GetMapping("/buscar/nuevo")
    public Optional<Libro> libroMasNuevo() {
        return libroService.obtenerLibroMasNuevo();
    }

    //@GetMapping("/")


}
