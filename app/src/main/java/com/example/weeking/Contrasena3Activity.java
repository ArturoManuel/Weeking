package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Contrasena3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrasena3);

        TextView textView = findViewById(R.id.volver);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(Contrasena3Activity.this, Cotrasena2Activity.class);
            startActivity(intent);
            finish();
        });
        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
    }
}