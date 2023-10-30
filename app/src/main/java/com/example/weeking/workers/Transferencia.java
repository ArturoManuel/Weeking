package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.weeking.R;

public class Transferencia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencia);

        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
        Button siguiente = findViewById(R.id.button10);
        siguiente.setOnClickListener(v -> {
            Intent intent = new Intent(this, Pago.class);
            startActivity(intent);
        });
    }
}