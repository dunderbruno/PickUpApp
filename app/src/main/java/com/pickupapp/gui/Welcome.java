package com.pickupapp.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.pickupapp.R;

public class Welcome extends AppCompatActivity {
    private TextView texto;
    protected static String nome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        texto = findViewById(R.id.textBoasVindas);
        texto.setText("Sejá Bem vindo "+ nome +"!");
    }
}
