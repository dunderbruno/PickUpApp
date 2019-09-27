package com.pickupapp.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pickupapp.R;
import com.pickupapp.dominio.User;
import com.pickupapp.infra.ValidacaoGui;
import com.pickupapp.negocio.UserNegocio;

public class Login extends AppCompatActivity {
    protected static String tipoUsuario;
    private Button voltar, acessar;
    private EditText login, senha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.inputLoginAcesso);
        senha = findViewById(R.id.inputSenhaAcesso);
        voltar = findViewById(R.id.buttonVoltarAcesso);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, MainLogin.class);
                startActivity(i);
                finish();
            }
        });
        acessar = findViewById(R.id.buttonAcessarAcesso);
        acessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validacao = validarCampos();
                if(validacao){
                    if(logar() != null){
                        Toast.makeText(Login.this,"Usuário logado",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(Login.this,"Usuário não cadastrado",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Login.this,"Cpf/Senha incorreto(s).",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private User logar(){
        UserNegocio negocio = new UserNegocio();
        String username = login.getText().toString().trim();
        String password = senha.getText().toString();
        return negocio.login(username, password);
    }

    public boolean validarCampos(){
        ValidacaoGui validacao = new ValidacaoGui();
        return validacao.validarCampoLogin(login) & validacao.validarCampoSenha(senha);
    }


}
