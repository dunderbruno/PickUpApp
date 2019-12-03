package com.pickupapp.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.pickupapp.R;
import com.pickupapp.dominio.Group;
import com.pickupapp.dominio.User;
import com.pickupapp.infra.Sessao;
import com.pickupapp.infra.ValidacaoGui;
import com.pickupapp.persistencia.UserInterface;
import com.pickupapp.persistencia.retorno.Token;
import com.pickupapp.persistencia.retorno.UserCall;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    private Button acessar;
    private EditText login, senha;
    private Spinner tipoUsuario;
    private TextView voltar;
    private ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.inputLoginAcesso);
        senha = findViewById(R.id.inputSenhaAcesso);
        tipoUsuario = findViewById(R.id.tipoDeUsuario2);
        voltar = findViewById(R.id.buttonVoltarAcesso);
        progress = findViewById(R.id.loginProgressBar);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
                finish();
            }
        });
        acessar = findViewById(R.id.buttonAcessarAcesso);
        acessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                boolean validacao = validarCampos();
                if(validacao){
                    final User usuario =new User();
                    usuario.setUsername(login.getText().toString());
                    usuario.setPassword(senha.getText().toString());
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
                    usuario.setGroup(new Group());
                    usuario.getGroup().setGroup_name(tipoUser);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://pickupbsiapi.herokuapp.com")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    UserInterface userInterface = retrofit.create(UserInterface.class);
                    String credentials = usuario.getUsername()+":"+
                                         usuario.getPassword();
                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(),
                            Base64.NO_WRAP);
                    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.e30.PODXncdn8smjXC-GZfhaMXIJ9M4fYAvwfZUT9xNgO3Y";
                    Call<Token> call = userInterface.login(auth,token,tipoUser);
                    call.enqueue(new Callback<Token>() {
                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            if (!response.isSuccessful()){
                                Log.d("resposta", "login: "+response);
                                progress.setVisibility(View.INVISIBLE);
                                return;
                            }
                            Token token = response.body();
                            usuario.setToken(token.getToken());
                            buscarId(usuario);
                            Log.d("resposta", "token: "+Sessao.getSessao(getApplicationContext()).getToken());

                        }

                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {
                            Log.d("resposta", "erro: "+t);
                            progress.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
    }

    public void buscarId(User usuario) {
        User resposta = usuario;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserInterface userInterface = retrofit.create(UserInterface.class);
        Call<UserCall> call = userInterface.buscarId(usuario.getUsername());
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
        String finalTipoUser = tipoUser;
        call.enqueue(new Callback<UserCall>() {
            @Override
            public void onResponse(Call<UserCall> call, Response<UserCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "login: "+response);
                    progress.setVisibility(View.INVISIBLE);
                    return;
                }
                UserCall token = response.body();
                resposta.setId(Integer.parseInt(token.getId()));
                Sessao sessao = new Sessao();
                sessao.editSessao(resposta, getApplicationContext());
                Intent i = null;
                if (finalTipoUser.equals("1")){
                    i = new Intent(Login.this, DrawerJogador.class);
                }else if (finalTipoUser.equals("2")){
                    i = new Intent(Login.this, DrawerLocador.class);
                }else{
                    i = new Intent(Login.this, DrawerArbitro.class);
                }
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<UserCall> call, Throwable t) {
                Log.d("resposta", "erro: "+t);
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }

    public boolean validarCampos(){
        ValidacaoGui validacao = new ValidacaoGui();
        return validacao.validarCampoLogin(login) & validacao.validarCampoSenha(senha);
    }


}
