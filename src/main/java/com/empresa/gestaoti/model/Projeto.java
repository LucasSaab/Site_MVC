package com.empresa.gestaoti.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * MODEL (M do MVC)
 * Representa um projeto de TI. É um POJO: só atributos,
 * construtores, getters e setters. Não sabe nada de HTML, rotas ou lista.
 */
public class Projeto {

    // ---------- Atributos ----------
    private Long id;
    private String nome;
    private String responsavel;
    private String descricao;
    private String status;       // "Planejado", "Em andamento" ou "Concluído"
    private String prioridade;   // "Baixa", "Média" ou "Alta"

    // Converte o texto do campo <input type="date"> (aaaa-mm-dd) em LocalDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate prazo;

    // ---------- Construtores ----------

    // Vazio: o Spring usa este para preencher o objeto com os dados do formulário
    public Projeto() {
        this.status = "Planejado";
        this.prioridade = "Média";
    }

    // Completo: usado pelo Controller para criar os dados de exemplo
    public Projeto(Long id, String nome, String responsavel, String descricao,
                   String status, String prioridade, LocalDate prazo) {
        this.id = id;
        this.nome = nome;
        this.responsavel = responsavel;
        this.descricao = descricao;
        this.status = status;
        this.prioridade = prioridade;
        this.prazo = prazo;
    }

    // ---------- Getters e setters ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }

    public LocalDate getPrazo() { return prazo; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }
}
