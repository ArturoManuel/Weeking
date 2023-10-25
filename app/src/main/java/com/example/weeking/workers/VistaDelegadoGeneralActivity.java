package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.weeking.R;

public class VistaDelegadoGeneralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_delegado_general);

        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());

        Button donadores = findViewById(R.id.lista);
        donadores.setOnClickListener(view -> {
            Intent intent= new Intent(this,Lista_don.class);
            startActivity(intent);
        });

    }
}