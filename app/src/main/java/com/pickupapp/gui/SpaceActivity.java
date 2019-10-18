package com.pickupapp.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.pickupapp.R;

public class SpaceActivity extends AppCompatActivity {
    private TextView nomeEspaco;
    private TextView phoneEspaco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);

        nomeEspaco = findViewById(R.id.nomedoespacoid);
        phoneEspaco = findViewById(R.id.phoneespaco);

        Bundle extras = getIntent().getExtras();
        nomeEspaco.setText(extras.getString("Nome"));
        phoneEspaco.setText(extras.getString("Phone"));
    }
}
