package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.R;

public class Donacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donacion);
        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
        RadioButton tranferencia = findViewById(R.id.transferencia);
        RadioButton plin = findViewById(R.id.plin);
        RadioButton yape = findViewById(R.id.yape);
        Button boton = findViewById(R.id.button8);
        boton.setOnClickListener(view -> {
            if (tranferencia.isChecked()) {
                Intent intent = new Intent(Donacion.this, Transferencia.class);
                startActivity(intent);
            } else if (plin.isChecked()) {
                Intent intent = new Intent(Donacion.this, Plin.class);
                startActivity(intent);
            } else if (yape.isChecked()) {
                Intent intent = new Intent(Donacion.this, Yape.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No has seleccionado ninguno", Toast.LENGTH_SHORT).show();
            }
        });

    }
}