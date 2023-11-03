package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.weeking.R;

public class Cotrasena2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotrasena2);
        Button button = findViewById(R.id.btnContinuar);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(Cotrasena2Activity.this, Contrasena3Activity.class);
            startActivity(intent);
            finish();
        });


    }
}