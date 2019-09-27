package com.pickupapp.infra;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    private static final String NOME_DO_BANCO = "BancoA2";
    private static final int VERSAO = 1;

    public DB(Context ctx) { super(ctx, NOME_DO_BANCO, null, VERSAO);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table usuario (id integer primary key autoincrement, username text not null, password text not null);");
        db.execSQL("create table pessoa (id integer primary key autoincrement, name text not null, surname text not null, idusuario integer);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table usuario;");
        db.execSQL("drop table pessoa;");

    }

    public void delete(SQLiteDatabase db){
        db.execSQL("drop table usuario");
        db.execSQL("drop table pessoa");

    }




}
