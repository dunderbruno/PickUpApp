package com.pickupapp.negocio;

import com.pickupapp.dominio.Person;
import com.pickupapp.dominio.User;
import com.pickupapp.infra.Criptografia;
import com.pickupapp.persistencia.PersonDAO;
import com.pickupapp.persistencia.UserDAO;

public class UserNegocio {

    public void inserirUsuario(User usuario, Person pessoa){
        UserDAO banco = new UserDAO();
        banco.inserirUsuario(usuario);
        usuario = recuperarUsuario(usuario.getUsername());
        pessoa.setUsuario(usuario);
        PersonNegocio negocio = new PersonNegocio();
        negocio.inserirPessoa(pessoa);


    }

    public boolean existeUsuario(String username){
        UserDAO banco = new UserDAO();
        return banco.existeUsuario(username);
    }

    public User verificarUsuario(String username, String senha){
        UserDAO banco = new UserDAO();
        return banco.verificarLogin(username, senha);
    }


    public User login(String username, String password){
        Criptografia criptografia = new Criptografia();
        String senhaCriptografada = criptografia.criptografarString(password);
        User usuario = verificarUsuario(username, senhaCriptografada);
        return usuario;
    }


    public User recuperarUsuario(String username){
        UserDAO banco = new UserDAO();
        return banco.recuperarUsuario(username);
    }
}