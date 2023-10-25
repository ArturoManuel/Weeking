package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.weeking.R;

public class Plin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plin);
        Button siguiente = findViewById(R.id.button9);
        siguiente.setOnClickListener(view -> {
            Intent intent= new Intent(this,Pago.class);
            startActivity(intent);
        });
        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
    }
}