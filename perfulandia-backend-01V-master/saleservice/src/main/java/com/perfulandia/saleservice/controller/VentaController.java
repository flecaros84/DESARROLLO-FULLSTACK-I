package com.perfulandia.saleservice.controller;

import com.perfulandia.saleservice.model.Venta;
import com.perfulandia.saleservice.model.Usuario; //se agrega para consulta externa
import com.perfulandia.saleservice.model.Producto; //se agrega para consulta externa
import com.perfulandia.saleservice.service.VentaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Nuevas importaciones DTO conexión al MS usuario
import org.springframework.web.client.RestTemplate;
//Para hacer peticiones HTTP a otros microservicios.

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    private final VentaService ventaService;
    private final RestTemplate restTemplate; // se declara rest template

    public VentaController(VentaService ventaService, RestTemplate restTemplate) {
        this.ventaService = ventaService;
        this.restTemplate = new RestTemplate(); // se inicia rest template
    }

    @GetMapping
    public List<Venta> listar() {
        return ventaService.listar();
    }

    @PostMapping
    public Venta guardar(@RequestBody Venta venta) {
        return ventaService.guardar(venta);
    }

    @GetMapping("/{id}")
    public Venta buscar(@PathVariable long id) {
        return ventaService.buscar(id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable long id) {
        ventaService.eliminar(id);
    }

    //Nuevo método para consultar microservicio usuarioservice
    @GetMapping("/usuario/{id}")
    public Usuario obtenerUsuario(@PathVariable long id){
        return restTemplate.getForObject("http://localhost:8081/api/usuarios/"+id,Usuario.class);
    }

    //Nuevo método para consultar microservicio productoservice
    @GetMapping("/producto/{id}")
    public Producto obtenerProducto(@PathVariable long id){
        return restTemplate.getForObject("http://localhost:8082/api/productos/"+id,Producto.class);
    }

}
