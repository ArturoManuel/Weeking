package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weeking.R;

public class EventosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        Button btnAddEvent =  findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(v -> {
            Intent intent =  new Intent(EventosActivity.this, NuevoEventoActivity.class);
            startActivity(intent);
            finish();
        });

        ImageButton imabtnVerEvento =  findViewById(R.id.imaBtnEvento);
        imabtnVerEvento.setOnClickListener(v -> {
            Intent intent =  new Intent(EventosActivity.this, VistaEventoActivity.class);
            startActivity(intent);
            finish();
        });

        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
    }
}