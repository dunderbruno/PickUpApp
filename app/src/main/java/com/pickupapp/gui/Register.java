package com.pickupapp.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pickupapp.R;

public class Register extends AppCompatActivity {
    protected static String tipoUsuario;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
