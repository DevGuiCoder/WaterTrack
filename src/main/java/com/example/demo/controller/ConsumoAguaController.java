package com.example.demo.controller;

import com.example.demo.model.ConsumoAgua;
import com.example.demo.service.ConsumoAguaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumo")
public class ConsumoAguaController {

    private final ConsumoAguaService consumoService;

    public ConsumoAguaController(ConsumoAguaService consumoService) {
        this.consumoService = consumoService;
    }

    /**
     * Registra um consumo de água para o usuário informado.
     */
    @PostMapping("/{usuarioId}")
    public ResponseEntity<?> registrarConsumo(@PathVariable Long usuarioId,
                                              @RequestParam int quantidade) {
        try {
            ConsumoAgua consumo = consumoService.registrarConsumo(usuarioId, quantidade);
            return ResponseEntity.status(HttpStatus.CREATED).body(consumo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao registrar consumo.");
        }
    }

    /**
     * Lista todos os consumos de água para um usuário.
     */
    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> listarConsumos(@PathVariable Long usuarioId) {
        try {
            List<ConsumoAgua> consumos = consumoService.listarConsumosPorUsuario(usuarioId);
            return ResponseEntity.ok(consumos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar consumos.");
        }
    }

    /**
     * Retorna o total de água consumida por um usuário.
     */
    @GetMapping("/{usuarioId}/total")
    public ResponseEntity<?> calcularTotalConsumido(@PathVariable Long usuarioId) {
        try {
            Integer total = consumoService.calcularTotalConsumido(usuarioId);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao calcular total consumido.");
        }
    }
}
