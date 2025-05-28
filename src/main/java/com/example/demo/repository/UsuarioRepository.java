package com.example.demo.repository;

import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca por email (case insensitive)
    Optional<Usuario> findByEmailIgnoreCase(String email);

    // Busca por email com query explícita
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<Usuario> buscarPorEmail(@Param("email") String email);

    // Verifica se email existe
    boolean existsByEmail(String email);
}