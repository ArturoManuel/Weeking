package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class VistaDelegadoGeneralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_delegado_general);

        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
    }
}