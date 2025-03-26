package br.insper.prova;

import br.insper.prova.curso.Curso;
import br.insper.prova.curso.CursoController;
import br.insper.prova.curso.CursoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class CursoControllerTest {

    @InjectMocks
    private CursoController cursoController;

    @Mock
    private CursoService cursoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cursoController).build();
    }

    @Test
    void testListarCursos() throws Exception {
        Curso curso = new Curso();
        curso.setId("1");
        curso.setTitulo("Curso 1");
        curso.setDescricao("Descrição");
        curso.setCargaHoraria(10);
        curso.setInstrutor("Instrutor");
        curso.setNomeUsuario("Usuario");
        curso.setEmailUsuario("usuario@exemplo.com");

        when(cursoService.listarCursos()).thenReturn(List.of(curso));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cursos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].titulo").value("Curso 1"));
    }

    @Test
    void testCadastrarCurso() throws Exception {
        // Criando o curso com os mesmos valores
        Curso curso = new Curso();
        curso.setId("1");
        curso.setTitulo("Curso 1");
        curso.setDescricao("Descrição");
        curso.setCargaHoraria(10);
        curso.setInstrutor("Instrutor");
        curso.setNomeUsuario("Usuario");
        curso.setEmailUsuario("usuario@exemplo.com");

        // Simulando o retorno do serviço quando o curso for cadastrado
        when(cursoService.cadastrarCurso(any(Curso.class), eq("usuario@exemplo.com"))).thenReturn(curso);

        // Enviando a requisição e verificando a resposta
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cursos")
                        .header("email", "usuario@exemplo.com")
                        .contentType("application/json")
                        .content("{\"id\":\"1\", \"titulo\":\"Curso 1\", \"descricao\":\"Descrição\", \"cargaHoraria\":10, \"instrutor\":\"Instrutor\", \"nomeUsuario\":\"Usuario\", \"emailUsuario\":\"usuario@exemplo.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.titulo").value("Curso 1"));
    }


    @Test
    void testExcluirCurso() throws Exception {
        doNothing().when(cursoService).excluirCurso("1", "usuario@exemplo.com");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cursos/1")
                        .header("email", "usuario@exemplo.com"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
