package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActividadesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);

        ImageButton imaBtnVerEvento =  findViewById(R.id.imaBtnVerEventos);
        imaBtnVerEvento.setOnClickListener(v -> {
            Intent intent =  new Intent(ActividadesActivity.this, EventosActivity.class);
            startActivity(intent);
            finish();
        });
        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
    }
}