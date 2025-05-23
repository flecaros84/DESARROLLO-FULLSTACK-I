package com.biblioteca.biblioteca.controller;

//import
import com.biblioteca.biblioteca.model.Prestamo;
import com.biblioteca.biblioteca.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitudes")

public class PrestamoController {

    @Autowired
    private PrestamoService servicioInyectado;

    //POST
    @PostMapping //Indicamos tipo de solicitudes POST
    public String crearPrestamo(@RequestBody Prestamo prestamo) {
        servicioInyectado.crearPrestamo(prestamo);
        return "La solicitud del prestamo se registro exitosamente";
    }

    @GetMapping
    public List<Prestamo> obtenerPrestamos() {
        return servicioInyectado.listarPrestamos();
    }


}
