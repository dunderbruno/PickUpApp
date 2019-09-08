package com.pickupapp.dominio;

public class Usuario {
    private long id;
    private String login;
    private String senha;
    private Pessoa pessoa;

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}
