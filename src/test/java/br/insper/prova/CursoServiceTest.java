package br.insper.prova;

import br.insper.prova.curso.Curso;
import br.insper.prova.curso.CursoRepository;
import br.insper.prova.curso.CursoService;
import br.insper.prova.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CursoServiceTest {

    @InjectMocks
    private CursoService cursoService;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private RestTemplate restTemplate;

    private Usuario usuario;
    private Curso curso;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuração do usuário mockado
        usuario = new Usuario();
        usuario.setPapel("ADMIN");
        usuario.setNome("Usuario Teste");
        usuario.setEmail("usuario@exemplo.com");

        // Configuração do curso mockado
        curso = new Curso();
        curso.setId("1");
        curso.setTitulo("Curso de Teste");
        curso.setDescricao("Curso para testes unitários");
        curso.setCargaHoraria(20);
        curso.setInstrutor("Instrutor Teste");
    }

    @Test
    void testListarCursos() {
        // Configura o comportamento do mock para listarCursos
        when(cursoRepository.findAll()).thenReturn(List.of(curso));

        // Chama o método listarCursos
        List<Curso> cursos = cursoService.listarCursos();

        // Verifica se o curso foi retornado corretamente
        assertNotNull(cursos);
        assertEquals(1, cursos.size());
        assertEquals(curso.getTitulo(), cursos.get(0).getTitulo());
    }

    @Test
    void testCadastrarCurso() {
        // Configura o comportamento do mock para buscar o usuário
        when(restTemplate.getForObject(anyString(), eq(Usuario.class))).thenReturn(usuario);

        // Configura o comportamento do mock para salvar o curso
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        // Chama o método cadastrarCurso
        Curso novoCurso = cursoService.cadastrarCurso(curso, "usuario@exemplo.com");

        // Verifica se o curso foi cadastrado corretamente
        assertNotNull(novoCurso);
        assertEquals(curso.getTitulo(), novoCurso.getTitulo());

        // Verifica se o método salvar foi chamado
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    void testCadastrarCursoUsuarioNaoAdmin() {
        // Simula o caso de um usuário não administrador
        usuario.setPapel("USER");

        // Configura o comportamento do mock para buscar o usuário
        when(restTemplate.getForObject(anyString(), eq(Usuario.class))).thenReturn(usuario);

        // Verifica se uma exceção é lançada quando o usuário não é administrador
        Exception exception = assertThrows(RuntimeException.class, () -> {
            cursoService.cadastrarCurso(curso, "usuario@exemplo.com");
        });

        assertEquals("Apenas administradores podem cadastrar cursos", exception.getMessage());
    }

    @Test
    void testExcluirCurso() {
        // Configura o comportamento do mock para buscar o usuário
        when(restTemplate.getForObject(anyString(), eq(Usuario.class))).thenReturn(usuario);

        // Configura o comportamento do mock para buscar o curso
        when(cursoRepository.findById(anyString())).thenReturn(Optional.of(curso));

        // Chama o método excluirCurso
        cursoService.excluirCurso("1", "usuario@exemplo.com");

        // Verifica se o método excluir foi chamado
        verify(cursoRepository, times(1)).deleteById(anyString());
    }

    @Test
    void testExcluirCursoUsuarioNaoAdmin() {
        // Simula o caso de um usuário não administrador
        usuario.setPapel("USER");

        // Configura o comportamento do mock para buscar o usuário
        when(restTemplate.getForObject(anyString(), eq(Usuario.class))).thenReturn(usuario);

        // Verifica se uma exceção é lançada quando o usuário não é administrador
        Exception exception = assertThrows(RuntimeException.class, () -> {
            cursoService.excluirCurso("1", "usuario@exemplo.com");
        });

        assertEquals("Apenas administradores podem excluir cursos", exception.getMessage());
    }

    @Test
    void testExcluirCursoNaoEncontrado() {
        // Configura o comportamento do mock para buscar o usuário
        when(restTemplate.getForObject(anyString(), eq(Usuario.class))).thenReturn(usuario);

        // Simula o caso de curso não encontrado
        when(cursoRepository.findById(anyString())).thenReturn(Optional.empty());

        // Verifica se uma exceção é lançada quando o curso não é encontrado
        Exception exception = assertThrows(RuntimeException.class, () -> {
            cursoService.excluirCurso("1", "usuario@exemplo.com");
        });

        assertEquals("Curso não encontrado", exception.getMessage());
    }
}
