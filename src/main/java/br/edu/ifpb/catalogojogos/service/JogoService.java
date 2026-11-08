package br.edu.ifpb.catalogojogos.service;

import br.edu.ifpb.catalogojogos.model.Jogo;
import br.edu.ifpb.catalogojogos.repository.JogoRepository;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class JogoService {

    private final JogoRepository jogoRepository;

    public JogoService(JogoRepository jogoRepository) {
        this.jogoRepository = jogoRepository;
    }

    // Cadastra um jogo
    public Jogo cadastrar(Jogo jogo) throws IOException {

        validarJogo(jogo);

        return jogoRepository.salvar(jogo);
    }

    // Retorna todos os jogos
    public List<Jogo> listar() throws IOException {

        return jogoRepository.listarTodos();
    }

    // Busca um jogo específico
    public Jogo buscar(String id) throws IOException {

        Jogo jogo = jogoRepository.buscarPorId(id);

        if (jogo == null) {
            throw new IllegalArgumentException("Jogo não encontrado.");
        }

        return jogo;
    }

    // Atualiza um jogo
    public Jogo atualizar(String id, Jogo jogo) throws IOException {

        validarJogo(jogo);

        if (!jogoRepository.atualizar(id, jogo)) {
            throw new IllegalArgumentException("Jogo não encontrado.");
        }

        return jogo;
    }

    // Exclui um jogo
    public void excluir(String id) throws IOException {

        if (!jogoRepository.excluir(id)) {
            throw new IllegalArgumentException("Jogo não encontrado.");
        }
    }

    // Verifica se os dados informados são válidos
    private void validarJogo(Jogo jogo) {

        if (jogo == null) {
            throw new IllegalArgumentException("Os dados do jogo são obrigatórios.");
        }

        if (jogo.getTitulo() == null || jogo.getTitulo().isBlank()) {
            throw new IllegalArgumentException("O título é obrigatório.");
        }

        if (jogo.getPlataforma() == null || jogo.getPlataforma().isBlank()) {
            throw new IllegalArgumentException("A plataforma é obrigatória.");
        }

        if (jogo.getGenero() == null || jogo.getGenero().isBlank()) {
            throw new IllegalArgumentException("O gênero é obrigatório.");
        }

        if (jogo.getDesenvolvedora() == null || jogo.getDesenvolvedora().isBlank()) {
            throw new IllegalArgumentException("A desenvolvedora é obrigatória.");
        }

        if (jogo.getAnoLancamento() < 1950 || jogo.getAnoLancamento() > 2100) {
            throw new IllegalArgumentException("Ano de lançamento inválido.");
        }

        if (jogo.getClassificacaoIndicativa() < 0) {
            throw new IllegalArgumentException("Classificação indicativa inválida.");
        }

        if (jogo.getNota() < 0 || jogo.getNota() > 10) {
            throw new IllegalArgumentException("A nota deve estar entre 0 e 10.");
        }
    }
}
