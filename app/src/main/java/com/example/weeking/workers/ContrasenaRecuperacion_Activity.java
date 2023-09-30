package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.weeking.R;

public class ContrasenaRecuperacion_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrasena_recuperacion);

        Button button = findViewById(R.id.btnContinuar);

        button.setOnClickListener(view -> {
            Intent intent = new Intent(ContrasenaRecuperacion_Activity.this, Cotrasena2Activity.class);
            startActivity(intent);
            finish();
        });

        TextView textView = findViewById(R.id.volver);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(ContrasenaRecuperacion_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();

        });

        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
    }
}