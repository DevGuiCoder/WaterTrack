package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class UsuarioControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuarioExemplo;

    @BeforeEach
    void setup() {
        usuarioExemplo = new Usuario();
        usuarioExemplo = new Usuario(1L, "teste@example.com", "Usuário Teste");
        usuarioExemplo.setEmail("teste@example.com");
        usuarioExemplo.setNome("Usuário Teste");
    }

    @Test
    void criarUsuario_quandoEmailNaoExiste_deveSalvarERetornarCreated() {
        // Arrange
        when(usuarioRepository.existsByEmail(usuarioExemplo.getEmail())).thenReturn(false);
        when(usuarioRepository.save(usuarioExemplo)).thenReturn(usuarioExemplo);

        // Act
        ResponseEntity<?> response = usuarioController.criarUsuario(usuarioExemplo);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(usuarioExemplo, response.getBody());
        verify(usuarioRepository).save(usuarioExemplo);
    }

    @Test
    void criarUsuario_quandoEmailExiste_deveRetornarConflict() {
        // Arrange
        when(usuarioRepository.existsByEmail(usuarioExemplo.getEmail())).thenReturn(true);

        // Act
        ResponseEntity<?> response = usuarioController.criarUsuario(usuarioExemplo);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("E-mail já cadastrado.", response.getBody());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void buscarPorId_quandoUsuarioExiste_deveRetornarUsuario() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExemplo));

        // Act
        ResponseEntity<Usuario> response = usuarioController.buscarPorId(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioExemplo, response.getBody());
    }

    @Test
    void buscarPorId_quandoUsuarioNaoExiste_deveRetornarNotFound() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Usuario> response = usuarioController.buscarPorId(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void buscarPorEmail_quandoUsuarioExiste_deveRetornarUsuario() {
        // Arrange
        when(usuarioRepository.findByEmailIgnoreCase("teste@example.com"))
                .thenReturn(Optional.of(usuarioExemplo));

        // Act
        ResponseEntity<Usuario> response = usuarioController.buscarPorEmail("teste@example.com");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioExemplo, response.getBody());
    }

    @Test
    void buscarPorEmail_quandoUsuarioNaoExiste_deveRetornarNotFound() {
        // Arrange
        when(usuarioRepository.findByEmailIgnoreCase("teste@example.com"))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<Usuario> response = usuarioController.buscarPorEmail("teste@example.com");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
