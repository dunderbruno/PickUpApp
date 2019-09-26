package com.pickupapp.infra;

import android.widget.EditText;

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

}
