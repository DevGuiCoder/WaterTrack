package com.example.demo.service;

import com.example.demo.model.ConsumoAgua;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ConsumoAguaRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsumoAguaService {

    private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado com ID: %d";

    private final ConsumoAguaRepository consumoRepository;
    private final UsuarioRepository usuarioRepository;

    public ConsumoAguaService(ConsumoAguaRepository consumoRepository,
                              UsuarioRepository usuarioRepository) {
        this.consumoRepository = consumoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ConsumoAgua registrarConsumo(Long usuarioId, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format(USUARIO_NAO_ENCONTRADO, usuarioId))
                );

        ConsumoAgua consumo = new ConsumoAgua();
        consumo.setUsuario(usuario);
        consumo.setQuantidadeMl(quantidade);
        // Precisa adicionar este campo na sua entidade
        consumo.setDataHora(LocalDateTime.now());

        return consumoRepository.save(consumo);
    }

    @Transactional(readOnly = true)
    public List<ConsumoAgua> listarConsumosPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format(USUARIO_NAO_ENCONTRADO, usuarioId)
            );
        }

        return consumoRepository.findByUsuarioIdOrderByDataHoraDesc(usuarioId);
    }

    @Transactional(readOnly = true)
    public Integer calcularTotalConsumido(Long usuarioId) {
        return consumoRepository.sumQuantidadeMlByUsuarioId(usuarioId)
                .orElse(0);
    }
}
