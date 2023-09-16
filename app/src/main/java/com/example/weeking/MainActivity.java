package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = findViewById(R.id.recuperarContraseña);
        text.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, ContrasenaRecuperacion_Activity.class);
            startActivity(intent);
        });

        TextView text2 = findViewById(R.id.registrate);
        text2.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });


    }
}