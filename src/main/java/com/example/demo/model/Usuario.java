package com.example.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String nome;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsumoAgua> consumos = new ArrayList<>();

    // Construtores
    public Usuario() {
    }

    public Usuario(Long id, String email, String nome) {
        this.id = id;
        this.email = email;
        this.nome = nome;
    }


    // Getters e Setters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (email.length() > 100) {
            throw new IllegalArgumentException("Email não pode ter mais de 100 caracteres");
        }
        this.email = email;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (nome.length() < 2 || nome.length() > 100) {
            throw new IllegalArgumentException("Nome deve ter entre 2 e 100 caracteres");
        }
        this.nome = nome;
    }

    public List<ConsumoAgua> getConsumos() {
        return consumos;
    }

    public void setConsumos(List<ConsumoAgua> consumos) {
        this.consumos = consumos;
    }

    // Método auxiliar para validar email
    private boolean isValidEmail(String email) {
        // Implementação simples de validação de email
        return email.matches(".+@.+\\..+");
    }
}