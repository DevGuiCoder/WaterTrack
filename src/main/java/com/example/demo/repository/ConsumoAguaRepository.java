package com.example.demo.repository;

import com.example.demo.model.ConsumoAgua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConsumoAguaRepository extends JpaRepository<ConsumoAgua, Long> {
    List<ConsumoAgua> findByUsuarioIdOrderByDataHoraDesc(Long usuarioId);

    @Query("SELECT SUM(c.quantidadeMl) FROM ConsumoAgua c WHERE c.usuario.id = :usuarioId")
    Optional<Integer> sumQuantidadeMlByUsuarioId(@Param("usuarioId") Long usuarioId);
}