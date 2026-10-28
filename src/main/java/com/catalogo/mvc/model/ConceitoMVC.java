package com.catalogo.mvc.model;

public class ConceitoMVC {
    private int id;
    private String componente;
    private String titulo;
    private String descricao;
    private String camada;

    public ConceitoMVC() {
    }

    public ConceitoMVC(int id, String componente, String titulo, String descricao, String camada) {
        this.id = id;
        this.componente = componente;
        this.titulo = titulo;
        this.descricao = descricao;
        this.camada = camada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComponente() {
        return componente;
    }

    public void setComponente(String componente) {
        this.componente = componente;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCamada() {
        return camada;
    }

    public void setCamada(String camada) {
        this.camada = camada;
    }
}
