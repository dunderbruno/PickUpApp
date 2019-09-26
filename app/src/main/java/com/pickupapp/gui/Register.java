package com.pickupapp.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pickupapp.R;
import com.pickupapp.infra.ValidacaoGui;

public class Register extends AppCompatActivity {
    protected static String tipoUsuario;
    private Button voltar, cadastrar;
    private EditText nome, sobrenome, login, senha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nome = findViewById(R.id.inputNomeCadastro);
        sobrenome = findViewById(R.id.inputSobrenomeCadastro);
        login = findViewById(R.id.inputLoginCadastro);
        senha = findViewById(R.id.inputSenhaCadastro);
        voltar = findViewById(R.id.buttonVoltarCadastro);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, MainRegister.class);
                startActivity(i);
                finish();
            }
        });
        cadastrar = findViewById(R.id.buttonCadastrarCadastro);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validacao = validarCampos();
                if(validacao){
                    //classe de negocio inserida para realizar o registro.
                    //Alterar nome da classe e função abaixo de acordo com a criada, e habilitar o codigo
//                    RegisterNegocio registro = new RegisterNegocio();
//                    boolean cadastro = registro.registrarUsuario() ;
//                    if (cadastro){
//                        Intent i = new Intent(Register.this, MainLogin.class);
//                        startActivity(i);
//                        finish();
//                    }else{
//                        Toast.makeText(getBaseContext(),"Não foi possivel realizar seu cadastro.",Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        });
    }

    public boolean validarCampos(){
        ValidacaoGui validacao = new ValidacaoGui();
        return validacao.validarCampoLogin(login) & validacao.validarCampoSenha(senha)
                & validacao.validarCampoNome(nome) & validacao.validarCampoNome(sobrenome);
    }
}
