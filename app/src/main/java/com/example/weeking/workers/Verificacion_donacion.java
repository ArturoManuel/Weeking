package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.weeking.R;

public class Verificacion_donacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion_donacion);

        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
    }
}