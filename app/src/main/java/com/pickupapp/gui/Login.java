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
import com.pickupapp.persistencia.UserDAO;

import org.json.JSONException;

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
                    //classe de negocio inserida para realizar o login.
                    //Alterar nome da classe e função abaixo de acordo com a criada, e habilitar o codigo
                    User usuario =new User();
                    usuario.setUsername(login.getText().toString());
                    usuario.setPassword(senha.getText().toString());
                    UserDAO login = new UserDAO(getBaseContext());
                    User acesso = null;
                    try {
                        acesso = login.login(usuario);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!acesso.getToken().equals("")){
                        Intent i = new Intent(Login.this, Welcome.class);
                        startActivity(i);
                        Welcome.nome = acesso.getUsername() + " " + acesso.getPassword();
                        finish();
                    }else{
                        Toast.makeText(getBaseContext(),"Não foi possivel realizar seu login.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public boolean validarCampos(){
        ValidacaoGui validacao = new ValidacaoGui();
        return validacao.validarCampoLogin(login) & validacao.validarCampoSenha(senha);
    }


}
