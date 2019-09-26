package com.pickupapp.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pickupapp.R;

public class MainScreen extends AppCompatActivity {
    private Button acessar, cadastrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        acessar = findViewById(R.id.button_acessar);
        acessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainScreen.this, MainLogin.class);
                startActivity(i);
            }
        });
        cadastrar = findViewById(R.id.button_cadastrar);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainScreen.this, MainRegister.class);
                startActivity(i);
            }
        });
    }
}
