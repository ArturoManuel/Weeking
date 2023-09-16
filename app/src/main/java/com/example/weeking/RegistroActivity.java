package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        TextView text = findViewById(R.id.tienescuenta);
        text.setOnClickListener( view -> {
            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        Button button = findViewById(R.id.btnRegister);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(RegistroActivity.this,EstadoActivity.class);
            startActivity(intent);
            finish();
        });

    }
}