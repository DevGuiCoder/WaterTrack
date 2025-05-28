import com.example.demo.model.ConsumoAgua;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ConsumoAguaRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.ConsumoAguaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConsumoAguaServiceTest {

    private ConsumoAguaRepository consumoRepository;
    private UsuarioRepository usuarioRepository;
    private ConsumoAguaService consumoAguaService;

    @BeforeEach
    public void setup() {
        consumoRepository = mock(ConsumoAguaRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        consumoAguaService = new ConsumoAguaService(consumoRepository, usuarioRepository);
    }

    @Test
    public void registrarConsumo_QuantidadeInvalida_LancaException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            consumoAguaService.registrarConsumo(1L, 0);
        });
        assertEquals("A quantidade deve ser maior que zero", ex.getMessage());
    }

    @Test
    public void registrarConsumo_UsuarioNaoEncontrado_LancaResponseStatusException() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            consumoAguaService.registrarConsumo(1L, 500);
        });

        assertTrue(ex.getMessage().contains("Usuário não encontrado"));
    }

    @Test
    public void registrarConsumo_Sucesso() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@exemplo.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        ArgumentCaptor<ConsumoAgua> captor = ArgumentCaptor.forClass(ConsumoAgua.class);
        when(consumoRepository.save(captor.capture())).thenAnswer(i -> i.getArgument(0));

        ConsumoAgua resultado = consumoAguaService.registrarConsumo(1L, 500);

        assertEquals(usuario, resultado.getUsuario());
        assertEquals(500, resultado.getQuantidadeMl());
        assertNotNull(resultado.getDataHora());
        verify(consumoRepository, times(1)).save(any(ConsumoAgua.class));
    }

    @Test
    public void listarConsumosPorUsuario_UsuarioNaoExiste_LancaException() {
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            consumoAguaService.listarConsumosPorUsuario(1L);
        });

        assertTrue(ex.getMessage().contains("Usuário não encontrado"));
    }

    @Test
    public void listarConsumosPorUsuario_Sucesso() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        ConsumoAgua c1 = new ConsumoAgua();
        c1.setQuantidadeMl(300);
        c1.setDataHora(LocalDateTime.now());

        ConsumoAgua c2 = new ConsumoAgua();
        c2.setQuantidadeMl(200);
        c2.setDataHora(LocalDateTime.now().minusHours(1));

        when(consumoRepository.findByUsuarioIdOrderByDataHoraDesc(1L)).thenReturn(Arrays.asList(c1, c2));

        List<ConsumoAgua> consumos = consumoAguaService.listarConsumosPorUsuario(1L);

        assertEquals(2, consumos.size());
        assertEquals(300, consumos.get(0).getQuantidadeMl());
        assertEquals(200, consumos.get(1).getQuantidadeMl());
    }

    @Test
    public void calcularTotalConsumido_RetornaSoma() {
        when(consumoRepository.sumQuantidadeMlByUsuarioId(1L)).thenReturn(Optional.of(1000));

        Integer total = consumoAguaService.calcularTotalConsumido(1L);

        assertEquals(1000, total);
    }

    @Test
    public void calcularTotalConsumido_SeNaoExistirRetornaZero() {
        when(consumoRepository.sumQuantidadeMlByUsuarioId(1L)).thenReturn(Optional.empty());

        Integer total = consumoAguaService.calcularTotalConsumido(1L);

        assertEquals(0, total);
    }
}
