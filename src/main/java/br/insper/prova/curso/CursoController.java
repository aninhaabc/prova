package br.insper.prova.curso;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public List<Curso> listarCursos() {
        return cursoService.listarCursos();
    }

    @PostMapping
    public ResponseEntity<Curso> cadastrarCurso(
            @RequestBody Curso curso,
            @RequestHeader(name = "email") String email) {
        Curso novoCurso = cursoService.cadastrarCurso(curso, email);
        return ResponseEntity.ok(novoCurso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCurso(
            @PathVariable String id,
            @RequestHeader(name = "email") String email) {
        cursoService.excluirCurso(id, email);
        return ResponseEntity.noContent().build();
    }
}
