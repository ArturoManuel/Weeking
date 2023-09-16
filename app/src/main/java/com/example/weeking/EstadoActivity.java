package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class EstadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado);

        TextView textView = findViewById(R.id.volver);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(EstadoActivity.this, RegistroActivity.class);
            startActivity(intent);
            finish();
        });
    }
}