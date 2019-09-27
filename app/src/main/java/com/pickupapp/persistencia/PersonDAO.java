package com.pickupapp.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pickupapp.dominio.Person;
import com.pickupapp.dominio.User;
import com.pickupapp.infra.DB;
import com.pickupapp.infra.PickUpApp;


public class PersonDAO {
    private SQLiteDatabase banco;

    public PersonDAO(){
        habilitarBanco(PickUpApp.getContext());
    }

    private SQLiteDatabase habilitarBanco(Context context){
        DB auxDB = new DB(context);
        banco = auxDB.getWritableDatabase();
        return banco;
    }

    public void inserirPessoa(Person pessoa){
        ContentValues valores = new ContentValues();
        valores.put("name", pessoa.getName());
        valores.put("surname", pessoa.getSurname());
        valores.put("idusuario", pessoa.getUsuario().getId());
        banco.insert("pessoa", null, valores);
        banco.close();
    }


    public Person recuperarPessoa(int id){
        Person pessoa = null;
        Cursor cursor = banco.query("pessoa", new String[]{"*"}, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if(cursor.getCount()>0){
            pessoa = montarPessoa(cursor);
        }
        return pessoa;
    }



    private Person montarPessoa(Cursor cursor){
        cursor.moveToFirst();
        Person pessoa = new Person();
        pessoa.setId(cursor.getInt(0));
        pessoa.setName(cursor.getString(1));
        pessoa.setSurname(cursor.getString(3));
        UserDAO bancoUsuario = new UserDAO();
        User usuario = bancoUsuario.recuperarUsuario(cursor.getInt(2));
        pessoa.setUsuario(usuario);
        return pessoa;
    }
}
