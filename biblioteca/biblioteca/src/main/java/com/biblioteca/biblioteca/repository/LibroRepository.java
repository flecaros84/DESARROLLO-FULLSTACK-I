package com.biblioteca.biblioteca.repository;
import com.biblioteca.biblioteca.model.Libro;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
@Repository//Marca esta clase como componente repositorio para spring
public class LibroRepository {
    private List<Libro> listaLibros = new ArrayList<>();

    //Constructor que carga 10 libros al iniciar
    public LibroRepository() {
        listaLibros.add(new Libro(1, "9789569646638", "Fuego y Sangre", "Penguin Random House Grupo Editorial", 2018, " George R. R. Martin"));
        listaLibros.add(new Libro(2, "9789563494150", "Quique Hache: El Mall Embrujado y Otras Historias", "Sm Ediciones", 2014, "Sergio Gomez"));
        listaLibros.add(new Libro(3, "9781484256251", "Spring Boot Persistence Best Practices", "Apress", 2020, "Anghel Leonard"));
        listaLibros.add(new Libro(4, "9789566075752", "Harry Potter y la piedra filosofal", "Salamandra", 2024, "J. K. Rowling"));
        listaLibros.add(new Libro(5, "9780439139601", "Harry Potter y el prisionero de Azkaban", "Scholastic", 1999, "J. K. Rowling"));
        listaLibros.add(new Libro(6, "9780439136365", "Harry Potter y el cáliz de fuego", "Scholastic", 2000, "J. K. Rowling"));
        listaLibros.add(new Libro(7, "9780321127426", "Effective Java", "Addison-Wesley", 2008, "Joshua Bloch"));
        listaLibros.add(new Libro(8, "9780134685991", "Clean Architecture", "Prentice Hall", 2017, "Robert C. Martin"));
        listaLibros.add(new Libro(9, "9780201633610", "Design Patterns", "Addison-Wesley", 1994, "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides"));
        listaLibros.add(new Libro(10, "9780132350884", "Clean Code", "Prentice Hall", 2008, "Robert C. Martin"));
    }
    public int totalLibros() {
        return listaLibros.size();
    }

    public Optional<Libro> buscarPorIsbn(String isbn) {
        return listaLibros.stream().filter(libro -> libro.getIsbn().equals(isbn)).findFirst();
    }

    public long contarPorAnio(int anio) {
        return listaLibros.stream().filter(libro -> libro.getAnioPublicacion() == anio).count();
    }

    public List<Libro> buscarPorAutor(String autor) {
        return listaLibros.stream()
                .filter(libro -> libro.getAutor().equalsIgnoreCase(autor))
                .collect(Collectors.toList());
    }

    public Optional<Libro> libroMasAntiguo() {
        return listaLibros.stream()
                .min(Comparator.comparingInt(Libro::getAnioPublicacion));
    }

    public Optional<Libro> libroMasNuevo() {
        return listaLibros.stream()
                .max(Comparator.comparingInt(Libro::getAnioPublicacion));
    }

    public List<Libro> listarOrdenadosPorAnio() {
        return listaLibros.stream()
                .sorted(Comparator.comparingInt(Libro::getAnioPublicacion))
                .collect(Collectors.toList());
    }
}
