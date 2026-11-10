package br.edu.ifpb.catalogojogos.controller;

import br.edu.ifpb.catalogojogos.model.Jogo;
import br.edu.ifpb.catalogojogos.service.JogoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/jogos")
public class JogoController {

    private final JogoService jogoService;

    public JogoController(JogoService jogoService) {
        this.jogoService = jogoService;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Jogo jogo)
            throws IOException {

        try {
            Jogo novoJogo = jogoService.cadastrar(jogo);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(novoJogo);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Jogo>> listar() throws IOException {

        return ResponseEntity.ok(jogoService.listar());
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<?> buscarPorGenero(@PathVariable String genero)
            throws IOException {

        try {
            return ResponseEntity.ok(jogoService.buscarPorGenero(genero));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/plataforma/{plataforma}")
    public ResponseEntity<?> buscarPorPlataforma(@PathVariable String plataforma)
            throws IOException {

        try {
            return ResponseEntity.ok(jogoService.buscarPorPlataforma(plataforma));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/desenvolvedora/{nome}")
    public ResponseEntity<?> buscarPorDesenvolvedora(@PathVariable String nome)
            throws IOException {

        try {
            return ResponseEntity.ok(jogoService.buscarPorDesenvolvedora(nome));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable String id)
            throws IOException {

        try {
            return ResponseEntity.ok(jogoService.buscar(id));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable String id,
            @Valid @RequestBody Jogo jogo) throws IOException {

        try {
            return ResponseEntity.ok(
                    jogoService.atualizar(id, jogo)
            );

        } catch (IllegalArgumentException e) {

            if ("Jogo não encontrado.".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(e.getMessage());
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable String id)
            throws IOException {

        try {
            jogoService.excluir(id);

            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}