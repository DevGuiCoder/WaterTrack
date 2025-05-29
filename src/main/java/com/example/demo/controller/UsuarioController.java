package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cria um novo usuário.
     */
    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        try {
            // Validações básicas (redundante se você confiar no setter, mas ajuda a capturar erros antes)
            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email não pode ser vazio");
            }
            if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nome não pode ser vazio");
            }

            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail já cadastrado.");
            }

            Usuario salvo = usuarioRepository.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);

        } catch (IllegalArgumentException e) {
            // Captura exceções de validação nos setters
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace(); // Log do erro para debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar usuário.");
        }
    }

    /**
     * Busca um usuário por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca um usuário por e-mail (ignorando maiúsculas/minúsculas).
     */
    @GetMapping("/buscar")
    public ResponseEntity<Usuario> buscarPorEmail(@RequestParam String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
