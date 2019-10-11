package com.pickupapp.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pickupapp.R;

public class MainLogin extends AppCompatActivity {
    private Button jogador, locador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        jogador = findViewById(R.id.button_go_loginJogador);
        jogador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainLogin.this, Login.class);
                startActivity(i);
                Login.tipoUsuario = "1";
                finish();
            }
        });
        locador = findViewById(R.id.button_go_loginLocador);
        locador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainLogin.this, Login.class);
                startActivity(i);
                Login.tipoUsuario = "2";
                finish();
            }
        });
    }
}
