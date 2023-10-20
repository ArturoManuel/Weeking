package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.weeking.R;

public class VistaEventoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_evento);
        Button btnVerFotos = findViewById(R.id.btnDonaciones);
        btnVerFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VistaEventoActivity.this, GaleriaEventos.class);
                startActivity(intent);
            }
        });

        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
    }
}