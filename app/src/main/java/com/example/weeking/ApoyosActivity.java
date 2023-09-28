package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ApoyosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apoyos);
        Button btnBarra = findViewById(R.id.btnBarra);
        btnBarra.setOnClickListener(v -> {
            Intent intent =  new Intent(ApoyosActivity.this, VistaApoyosActivity.class);
            startActivity(intent);
            finish();
        });

        Button btnParticipantes = findViewById(R.id.btnParticipantes);
        btnParticipantes.setOnClickListener(v -> {
            Intent intent = new Intent(ApoyosActivity.this, VistaApoyosActivity.class);
            startActivity(intent);
            finish();
        });

        Button btnNuevosAlumnos = findViewById(R.id.btnNuevosAlumnos);
        btnNuevosAlumnos.setOnClickListener(v -> {
            Intent intent = new Intent(ApoyosActivity.this, VistaApoyosActivity.class);
            startActivity(intent);
            finish();
        });

        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
    }

}