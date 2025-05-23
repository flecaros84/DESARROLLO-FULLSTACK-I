package com.example.com.perfulandia.usarioservice.repository;

import com.example.com.perfulandia.usarioservice.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
}
