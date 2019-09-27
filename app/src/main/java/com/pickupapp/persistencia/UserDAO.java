package com.pickupapp.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pickupapp.dominio.User;
import com.pickupapp.infra.DB;
import com.pickupapp.infra.PickUpApp;


public class UserDAO {
    private SQLiteDatabase banco;
    public UserDAO(){
        habilitarBanco(PickUpApp.getContext());
    }

    private SQLiteDatabase habilitarBanco(Context ctx){
        DB auxDB = new DB(ctx);
        banco = auxDB.getWritableDatabase();
        return banco;
    }

    public void inserirUsuario(User usuario){
        ContentValues valores = new ContentValues();
        valores.put("username", usuario.getUsername());
        valores.put("password", usuario.getPassword());
        banco.insert("usuario", null, valores);
        banco.close();
    }

    public boolean existeUsuario(String login){
        Cursor cursor = banco.query("usuario", new String[]{"*"}, "login = ?", new String[]{login}, null, null, null);
        boolean resposta = false;
        if(cursor.getCount() > 0){
            resposta = true;
        }
        banco.close();
        cursor.close();
        return resposta;
    }

    public User verificarLogin(String username, String password){
        User usuario;
        Cursor cursor = banco.query("usuario", new String[]{"*"}, "username = ? and password = ?", new String[]{username, password}, null, null, null);
        if(cursor.getCount() > 0){
            usuario = montarUsuario(cursor);
        }else{
            usuario = null;
        }
        cursor.close();
        banco.close();
        return usuario;
    }

    private User montarUsuario(Cursor cursor) {
        User usuario = new User();
        cursor.moveToFirst();
        usuario.setId(cursor.getInt(0));
        usuario.setPassword(cursor.getString(1));
        usuario.setUsername(cursor.getString(2));
        return usuario;
    }

    public User recuperarUsuario(String username){
        User usuario;
        Cursor cursor = banco.query("usuario", new String[]{"*"}, "username = ?", new String[]{username}, null, null, null);
        if(cursor.getCount() > 0){
            usuario = montarUsuario(cursor);
        }else{
            usuario = null;
        }
        cursor.close();
        banco.close();
        return usuario;
    }

    public User recuperarUsuario(int id){
        User usuario;
        Cursor cursor = banco.query("usuario", new String[]{"*"}, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if(cursor.getCount()>0){
            usuario = montarUsuario(cursor);
        }else{
            usuario = null;
        }
        banco.close();
        cursor.close();
        return usuario;
    }

    public void alterarSenhaUsuario(User usuario) {
        String where = "id = ?";
        ContentValues valores = new ContentValues();
        valores.put("username", usuario.getUsername());
        valores.put("password", usuario.getPassword());
        banco.update("usuario", valores, where, new String[]{String.valueOf(usuario.getId())});
        banco.close();
    }
}
