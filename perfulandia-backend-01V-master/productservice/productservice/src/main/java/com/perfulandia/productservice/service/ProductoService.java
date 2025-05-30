package com.perfulandia.productservice.service;

import com.perfulandia.productservice.model.Producto;
import com.perfulandia.productservice.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    public final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    //listar
    public List<Producto> listar() {
        return repo.findAll();
    }

    //Guardar
    public Producto guardar(Producto producto) {
        return repo.save(producto);
    }

    //Buscar
    public Producto bucarPorId(long id) {
        return repo.findById(id).orElse(null);
    }

    //Eliminar
    public void eliminar(long id) {
        repo.deleteById(id);
    }

    //Descontar stock
    public Producto descontarStock(Long id, int cantidad) {
        Producto producto = bucarPorId(id);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }

        if (producto.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente para producto ID: " + id);
        }

        producto.setStock(producto.getStock() - cantidad);
        return repo.save(producto);
    }
}

