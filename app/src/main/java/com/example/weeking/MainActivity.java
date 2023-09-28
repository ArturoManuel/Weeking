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
        TextView text2 = findViewById(R.id.registrate);
        TextView text = findViewById(R.id.recuperarContraseña);



        text.setOnClickListener(v -> navigateToActivity(ContrasenaRecuperacion_Activity.class));
        text2.setOnClickListener(v -> navigateToActivity(RegistroActivity.class));


    }

    private void navigateToActivity(Class<?> destinationClass) {
        Intent intent = new Intent(MainActivity.this, destinationClass);
        startActivity(intent);
    }
}