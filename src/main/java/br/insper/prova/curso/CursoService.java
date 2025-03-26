package br.insper.prova.curso;

import br.insper.prova.usuario.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

@Service
public class CursoService {
    private final CursoRepository cursoRepository;
    private final RestTemplate restTemplate;

    public CursoService(CursoRepository cursoRepository, RestTemplate restTemplate) {
        this.cursoRepository = cursoRepository;
        this.restTemplate = restTemplate;
    }

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public Curso cadastrarCurso(Curso curso, String email) {
        Usuario usuario = buscarUsuario(email);
        if (!"ADMIN".equals(usuario.getPapel())) {
            throw new RuntimeException("Apenas administradores podem cadastrar cursos");
        }
        curso.setNomeUsuario(usuario.getNome());
        curso.setEmailUsuario(usuario.getEmail());
        return cursoRepository.save(curso);
    }

    public void excluirCurso(String id, String email) {
        Usuario usuario = buscarUsuario(email);
        if (!"ADMIN".equals(usuario.getPapel())) {
            throw new RuntimeException("Apenas administradores podem excluir cursos");
        }
        Optional<Curso> curso = cursoRepository.findById(id);
        if (curso.isEmpty()) {
            throw new RuntimeException("Curso não encontrado");
        }
        cursoRepository.deleteById(id);
    }

    private Usuario buscarUsuario(String email) {
        String url = "http://18.231.172.109:8080/api/usuario/" + email;
        try {
            return restTemplate.getForObject(url, Usuario.class);
        } catch (Exception e) {
            throw new RuntimeException("Usuário não encontrado");
        }
    }
}
