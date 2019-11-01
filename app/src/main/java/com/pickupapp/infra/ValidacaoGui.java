package com.pickupapp.infra;

import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidacaoGui {

    public boolean validarCampoLogin(EditText login){
        String logint = login.getText().toString();
        if (logint.equals("")){
            login.setError("Favor preencher com um login valido");
            return false;
        }
        else if (logint.length() < 6){
            login.setError("Favor preencher com um login valido");
            return false;
        }
        return true;
    }

    public boolean validarCampoNome(EditText nome){
        String nomet = nome.getText().toString();
        if (nomet.equals("")){
            nome.setError("Favor preencher com um login valido");
            return false;
        }
        else if (nomet.length() < 3){
            nome.setError("Favor preencher com um login valido");
            return false;
        }
        return true;
    }

    public boolean validarCampoSenha(EditText senha){
        String senhat = senha.getText().toString();
        if (senhat.equals("")){
            senha.setError("Favor preencher com um senha valida");
            return false;
        }
        else if (senhat.length() < 8){
            senha.setError("Favor preencher com uma senha valida");
            return false;
        }
        return true;
    }

    public boolean verificarTamanhoCampo(String campo, int min, int max) {
        return min <= campo.length() && campo.length() <= max;
    }

    public boolean verificarCampoEmail(String email) {
        String regex = "^((?!.*?\\.\\.)[A-Za-z0-9.\\!#\\$\\%\\&\\'*\\+\\-\\" +
                "/\\=\\?\\^_`\\{\\|\\}\\~]+@[A-Za-z0-9]+[A-Za-z0-9\\" +
                "-\\.]+\\.[A-Za-z0-9\\-\\.]+[A-Za-z0-9]+)$";
        return email.matches(regex);
    }

    public boolean verificarValor(String valor){
        String [] aux = valor.split(" ");
        return Float.parseFloat(aux[1])>0;
    }

    public boolean verificaHora (EditText inicio, EditText fim) throws ParseException {
        String StringInicio = inicio.getText().toString();
        String StringFim = fim.getText().toString();
        SimpleDateFormat simple = new SimpleDateFormat("HH:mm:ss");
        simple.setLenient(false);
        try{
            Date dateInicio = simple.parse(StringInicio);
            Date dateFim = simple.parse(StringFim);
            if (dateFim.compareTo(dateInicio) == 1 ){
                return true;
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        inicio.setError("Horário inválido");
        fim.setError("Horário inválido");
        return false;
    }
}
