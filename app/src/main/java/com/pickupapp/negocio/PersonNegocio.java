package com.pickupapp.negocio;

import com.pickupapp.dominio.Person;
import com.pickupapp.persistencia.PersonDAO;

public class PersonNegocio {

    public void inserirPessoa(Person pessoa){
    PersonDAO banco = new PersonDAO();
    banco.inserirPessoa(pessoa);
}


    public Person recuperarPessoaPorId(int id){
        PersonDAO banco = new PersonDAO();
        Person pessoa = banco.recuperarPessoa(id);
        return pessoa;
    }



}
