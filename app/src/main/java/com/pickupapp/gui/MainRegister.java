package com.pickupapp.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pickupapp.R;

public class MainRegister extends AppCompatActivity {
    private Button jogador, locador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);
        jogador = findViewById(R.id.button_go_registerJogador);
        jogador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainRegister.this, Register.class);
                startActivity(i);
                Register.tipoUsuario = "jogador";
                finish();
            }
        });
        locador = findViewById(R.id.button_go_registerLocador);
        locador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainRegister.this, Register.class);
                startActivity(i);
                Register.tipoUsuario = "locador";
                finish();
            }
        });
    }
}
