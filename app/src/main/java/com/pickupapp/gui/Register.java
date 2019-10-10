package com.pickupapp.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pickupapp.R;
import com.pickupapp.dominio.Group;
import com.pickupapp.dominio.Person;
import com.pickupapp.dominio.User;
import com.pickupapp.infra.Sessao;
import com.pickupapp.infra.ValidacaoGui;
import com.pickupapp.persistencia.UserDAO;

import org.json.JSONException;

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
                Toast.makeText(getBaseContext(),"teste",Toast.LENGTH_LONG).show();
                boolean validacao = validarCampos();
                if(validacao){
                    final User usuario = new User();
                    usuario.setUsername(login.getText().toString());
                    usuario.setPassword(senha.getText().toString());
                    Person pessoa = new Person();
                    pessoa.setName(nome.getText().toString());
                    pessoa.setSurname(sobrenome.getText().toString());
                    usuario.setPerson(pessoa);
                    Group grupo = new Group();
                    usuario.setGroup(grupo);
                    usuario.getGroup().setGroup_name(tipoUsuario);
                    final UserDAO registro = new UserDAO(getBaseContext());
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                User response = registro.register(usuario);
                                Sessao sessao= new Sessao();
                                sessao.editSessao(response, getBaseContext());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    thread.start();
                    Log.d("resposta", "onClick: "+ Sessao.getSessao(getBaseContext()).getId());
                    if(Sessao.getSessao(getBaseContext()).getId()!= -1 && Sessao.getSessao(getBaseContext()).getId()!= 0){
                        Intent i = new Intent(Register.this, MainScreen.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(getBaseContext(),"Não foi possivel realizar seu cadastro.",Toast.LENGTH_SHORT).show();
                    }
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
