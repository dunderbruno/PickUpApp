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
        Call<User> call = userInterface.register(params);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "cadastro Usuario: "+response);
                    return;
                }
                User user = response.body();
                setGrupoUsuario(user, usuario);
                criarPessoa(user, usuario);
                Intent i = new Intent(Register.this, MainScreen.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("resposta", "erro: "+t);
                Toast.makeText(getBaseContext(),"Não foi possivel realizar seu cadastro.",Toast.LENGTH_SHORT).show();

                cadastrar.setEnabled(true);
                voltar.setEnabled(true);
                login.setEnabled(true);
                senha.setEnabled(true);
            }
        });
    }

    private void criarPessoa(final User user, final User usuario) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PersonInterface personInterface = retrofit.create(PersonInterface.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", usuario.getPerson().getName());
        params.put("surname", usuario.getPerson().getName());
        Call<Person> call = personInterface.criarPessoa(params);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "cadastro Pessoa: "+response);
                    return;
                }
                Log.d("resposta", "cadastro Pessoa: "+response.body());
                Person pessoa = response.body();
                pessoa.getId();
                Log.d("resposta", "cadastro Pessoa: "+pessoa.getId());
                setPessoaUsuario(pessoa, user);
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
            }
        });
    }

    private void setGrupoUsuario(User user, User usuario) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserInterface userInterface = retrofit.create(UserInterface.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("group", usuario.getGroup().getGroup_name());
        Call<User> callset = userInterface.setGrupoUsuario(params, user.getId());
        callset.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "set Grupo: "+response);
                    return;
                }
                Log.d("resposta", "set Grupo: "+response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("resposta", "onResponse: "+t);
            }
        });
    }

    private void setPessoaUsuario(Person person, User usuario) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PersonInterface personInterface = retrofit.create(PersonInterface.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("person_id", String.valueOf(person.getId()));
        Call<Person> callset = personInterface.setPessoaUsuario(params, usuario.getId());
        callset.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "set Pessoa: "+response);
                    return;
                }
                Log.d("resposta", "set Pessoa: "+response);
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
            }
        });

    }

    public boolean validarCampos(){
        ValidacaoGui validacao = new ValidacaoGui();
        return validacao.validarCampoLogin(login) & validacao.validarCampoSenha(senha)
                & validacao.validarCampoNome(nome) & validacao.validarCampoNome(sobrenome);
    }
}
