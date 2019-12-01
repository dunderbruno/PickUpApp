package com.pickupapp.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pickupapp.R;
import com.pickupapp.dominio.Group;
import com.pickupapp.dominio.Person;
import com.pickupapp.dominio.User;
import com.pickupapp.infra.ValidacaoGui;
import com.pickupapp.persistencia.PersonInterface;
import com.pickupapp.persistencia.UserInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {
    private Button voltar, cadastrar;
    private EditText nome, sobrenome, login, senha;
    private Spinner tipoUsuario;
    private ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nome = findViewById(R.id.inputNomeCadastro);
        sobrenome = findViewById(R.id.inputSobrenomeCadastro);
        login = findViewById(R.id.inputLoginCadastro);
        senha = findViewById(R.id.inputSenhaCadastro);
        tipoUsuario = findViewById(R.id.tipoDeUsuario);
        cadastrar = findViewById(R.id.buttonCadastrarCadastro);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
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
                    TextView textView = (TextView)tipoUsuario.getSelectedView();
                    String result = textView.getText().toString();
                    usuario.getGroup().setGroup_name(result);
                    cadastrarUsuario(usuario);
                }
            }
        });
    }

    private void cadastrarUsuario(final User usuario) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserInterface userInterface = retrofit.create(UserInterface.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", usuario.getUsername());
        params.put("password", usuario.getPassword());
        params.put("name", usuario.getPerson().getName());
        params.put("surname", usuario.getPerson().getSurname());
        String tipoUser = "";
        TextView textView = (TextView)tipoUsuario.getSelectedView();
        String result = textView.getText().toString();
        switch (result){
            case "Jogador":
                tipoUser = "1";
                break;
            case "Locador":
                tipoUser = "2";
                break;
            case "Árbitro":
                tipoUser = "3";
                break;
        }
        params.put("group_id", tipoUser);
        Call<User> call = userInterface.register(params);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    progress.setVisibility(View.INVISIBLE);
                    Log.d("resposta", "cadastro Usuario: "+response);
                    return;
                }
                progress.setVisibility(View.INVISIBLE);
                Intent i = new Intent(Register.this, MainScreen.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("resposta", "erro: "+t);
                Toast.makeText(getBaseContext(),"Não foi possivel realizar seu cadastro.",Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }

    public boolean validarCampos(){
        ValidacaoGui validacao = new ValidacaoGui();
        return validacao.validarCampoLogin(login) & validacao.validarCampoSenha(senha)
                & validacao.validarCampoNome(nome) & validacao.validarCampoNome(sobrenome);
    }
}
