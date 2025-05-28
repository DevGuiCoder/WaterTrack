package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "consumo_agua")
public class ConsumoAgua {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "quantidade_ml", nullable = false)
    private Integer quantidadeMl;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    // Construtores
    public ConsumoAgua() {
    }

    public ConsumoAgua(Usuario usuario, Integer quantidadeMl, LocalDateTime dataHora) {
        this.setUsuario(usuario);
        this.setQuantidadeMl(quantidadeMl);
        this.setDataHora(dataHora);
    }

    // Getters e Setters com validação
    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário é obrigatório");
        }
        this.usuario = usuario;
    }

    public Integer getQuantidadeMl() {
        return quantidadeMl;
    }

    public void setQuantidadeMl(Integer quantidadeMl) {
        if (quantidadeMl == null) {
            throw new IllegalArgumentException("Quantidade de água é obrigatória");
        }
        if (quantidadeMl < 1) {
            throw new IllegalArgumentException("Quantidade de água deve ser no mínimo 1ml");
        }
        this.quantidadeMl = quantidadeMl;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        if (dataHora == null) {
            throw new IllegalArgumentException("Data/hora é obrigatória");
        }
        this.dataHora = dataHora;
    }

    // equals e hashCode baseados no ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumoAgua that = (ConsumoAgua) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
