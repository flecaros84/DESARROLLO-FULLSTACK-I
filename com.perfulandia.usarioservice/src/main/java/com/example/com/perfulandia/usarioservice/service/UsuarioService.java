package com.example.com.perfulandia.usarioservice.service;

import com.example.com.perfulandia.usarioservice.model.Usuario;
import com.example.com.perfulandia.usarioservice.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class UsuarioService {

    private final UsuarioRepository repo;

    //Constructor apra poder consumir la interfaz
    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;

    }

    //Listar
    public List<Usuario> listar() {
        return repo.findAll();
    }

    //Guardar
    public Usuario guardar(Usuario usuario) {
        return repo.save(usuario);
    }

    //Buscar
    public Usuario buscar(long id) {
        return repo.findById(id).orElse(null);
    }

    //Eliminar
    public void eliminar(long id) {
        repo.deleteById(id);
    }

}
