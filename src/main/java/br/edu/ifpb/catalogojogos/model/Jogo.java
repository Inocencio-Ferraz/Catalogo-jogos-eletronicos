package br.edu.ifpb.catalogojogos.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public class Jogo {

    private String id;
    @NotBlank(message = "O título é obrigatório.")
    private String titulo;
    @NotBlank(message = "A plataforma é obrigatória.")
    private String plataforma;
    @NotBlank(message = "O gênero é obrigatório.")
    private String genero;
    @NotBlank(message = "A desenvolvedora é obrigatória.")
    private String desenvolvedora;
    @Min(value = 1950, message = "Ano de lançamento inválido.")
    @Max(value = 2100, message = "Ano de lançamento inválido.")
    private int anoLancamento;
    @Min(value = 0, message = "Classificação indicativa inválida.")
    private int classificacaoIndicativa;
    @DecimalMin(value = "0.0", message = "A nota deve estar entre 0 e 10.")
    @DecimalMax(value = "10.0", message = "A nota deve estar entre 0 e 10.")
    private double nota;
    private String descricao;

    public Jogo() {
    }

    public Jogo(String id, String titulo, String plataforma,
                String genero, String desenvolvedora,
                int anoLancamento, int classificacaoIndicativa,
                double nota, String descricao) {

        this.id = id;
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.genero = genero;
        this.desenvolvedora = desenvolvedora;
        this.anoLancamento = anoLancamento;
        this.classificacaoIndicativa = classificacaoIndicativa;
        this.nota = nota;
        this.descricao = descricao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDesenvolvedora() {
        return desenvolvedora;
    }

    public void setDesenvolvedora(String desenvolvedora) {
        this.desenvolvedora = desenvolvedora;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public int getClassificacaoIndicativa() {
        return classificacaoIndicativa;
    }

    public void setClassificacaoIndicativa(int classificacaoIndicativa) {
        this.classificacaoIndicativa = classificacaoIndicativa;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
