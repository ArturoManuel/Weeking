package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Lista_don extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_don);

        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
    }
}