package com.perfulandia.productservice.controller;

import com.perfulandia.productservice.model.Usuario;
import com.perfulandia.productservice.model.Producto;
import com.perfulandia.productservice.service.ProductoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService servicio;
    private final RestTemplate restTemplate;

    public ProductoController(ProductoService servicio, RestTemplate restTemplate) {
        this.servicio = servicio;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public List<Producto> listar() {
        return servicio.listar();
    }

    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        return servicio.guardar(producto);
    }

    @GetMapping("/{id}")
    public Producto buscar(@PathVariable long id) {
        return servicio.bucarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable long id) {
        servicio.eliminar(id);
    }

    @GetMapping("/usuario/{id}")
    public Usuario obtenerUsuario(@PathVariable long id) {
        return restTemplate.getForObject("http://localhost:8081/api/usuarios/" + id, Usuario.class);
    }

    // ✅ Método actualizado para usar PUT (en lugar de PATCH)
    @PutMapping("/{id}/descontar")
    public Producto descontarStock(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        int cantidad = body.get("cantidad");
        return servicio.descontarStock(id, cantidad);
    }
}
