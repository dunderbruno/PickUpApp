package com.pickupapp.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pickupapp.R;
import com.pickupapp.dominio.Person;
import com.pickupapp.dominio.User;
import com.pickupapp.infra.Criptografia;
import com.pickupapp.infra.ValidacaoGui;
import com.pickupapp.negocio.UserNegocio;

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
                    criarConta();
                }
            }
        });
    }

    private void criarConta() {
        UserNegocio negocio = new UserNegocio();
        User usuario = new User();
        String loginString = login.getText().toString().trim();
        String password = senha.getText().toString();
        usuario.setPassword(password);
        usuario.setUsername(loginString);
        if (negocio.existeUsuario(loginString)) {
            Toast.makeText(this, "Cpf já registrado", Toast.LENGTH_SHORT).show();

        }else{
            inserirUsuario();

        }
    }

    public boolean validarCampos(){
        ValidacaoGui validacao = new ValidacaoGui();
        return validacao.validarCampoLogin(login) & validacao.validarCampoSenha(senha)
                & validacao.validarCampoNome(nome) & validacao.validarCampoNome(sobrenome);
    }

    private void inserirUsuario(){

        String nomeRegistro = nome.getText().toString().trim();
        String sobrenomeRegistro = sobrenome.getText().toString();
        String loginRegistro = login.getText().toString().trim();
        String senhaRegistro = senha.getText().toString().trim();
        User usuario = new User();
        usuario.setUsername(loginRegistro);
        Criptografia criptografia = new Criptografia();
        String senhaCriptografada = criptografia.criptografarString(senhaRegistro);
        usuario.setPassword(senhaCriptografada);
        Person pessoa = new Person();
        pessoa.setName(nomeRegistro);
        pessoa.setSurname(sobrenomeRegistro);
        UserNegocio negocio = new UserNegocio();
        negocio.inserirUsuario(usuario, pessoa);
        Toast.makeText(Register.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
        Register.this.finish();
    }
}
