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

import com.pickupapp.R;
import com.pickupapp.dominio.Group;
import com.pickupapp.dominio.User;
import com.pickupapp.infra.Sessao;
import com.pickupapp.infra.ValidacaoGui;
import com.pickupapp.persistencia.UserInterface;
import com.pickupapp.persistencia.retorno.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        final FrameLayout progress = findViewById(R.id.progressBarHolder);
        acessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                boolean validacao = validarCampos();
                if(validacao){
                    acessar.setEnabled(false);
                    voltar.setEnabled(false);
                    login.setEnabled(false);
                    senha.setEnabled(false);
                    final User usuario =new User();
                    usuario.setUsername(login.getText().toString());
                    usuario.setPassword(senha.getText().toString());
                    Group group = new Group();
                    group.setGroup_name(tipoUsuario);
                    usuario.setGroup(group);
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
                    Call<Token> call = userInterface.login(auth,token,tipoUsuario);
                    call.enqueue(new Callback<Token>() {
                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            if (!response.isSuccessful()){
                                Log.d("resposta", "login: "+response);
                                acessar.setEnabled(true);
                                voltar.setEnabled(true);
                                login.setEnabled(true);
                                senha.setEnabled(true);
                                return;
                            }
                            Token token = response.body();
                            usuario.setToken(token.getToken());
                            Sessao sessao = new Sessao();
                            sessao.editSessao(usuario, getApplicationContext());
                            Log.d("resposta", "token: "+response.body());
                            Intent i = null;
                            if (tipoUsuario.equals("1")){
                                i = new Intent(Login.this, DrawerJogador.class);
                            }else if (tipoUsuario.equals("2")){
                                i = new Intent(Login.this, DrawerLocador.class);
                            }else{
                                i = new Intent(Login.this, DrawerArbitro.class);
                            }
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {
                            Log.d("resposta", "erro: "+t);
                            acessar.setEnabled(true);
                            voltar.setEnabled(true);
                            login.setEnabled(true);
                            senha.setEnabled(true);
                        }
                    });
                }
                progress.setVisibility(View.GONE);
            }
        });
    }

    public boolean validarCampos(){
        ValidacaoGui validacao = new ValidacaoGui();
        return validacao.validarCampoLogin(login) & validacao.validarCampoSenha(senha);
    }


}
