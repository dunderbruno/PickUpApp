package com.pickupapp.dominio;

public class Pessoa {
    private long id;
    private String nome;
    private String cpf;
    // TODO: dataNascimento
    // TODO: private Contato contato
    // TODO: private Genero genero

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
