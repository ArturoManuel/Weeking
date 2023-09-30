package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.weeking.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        TextView text2 = findViewById(R.id.registrate);
//        TextView text = findViewById(R.id.recuperarContraseña);
//        Button inicio = findViewById(R.id.iniciarSesion);
//
//        text.setOnClickListener(v -> navigateToActivity(ContrasenaRecuperacion_Activity.class));
//       // text2.setOnClickListener(v -> navigateToActivity(RegistroActivity.class));
//        text2.setOnClickListener(v -> navigateToActivity(Stadistics.class));
//        inicio.setOnClickListener(v-> navigateToActivity(VistaPrincipal.class));
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.iniciarSesion.setOnClickListener(v -> navigateToActivity(VistaPrincipal.class));
        binding.recuperarContrasena.setOnClickListener(v-> navigateToActivity(ContrasenaRecuperacion_Activity.class));
        binding.registrate.setOnClickListener(v->navigateToActivity(RegistroActivity.class));
    }

    private void navigateToActivity(Class<?> destinationClass) {
        Intent intent = new Intent(MainActivity.this, destinationClass);
        startActivity(intent);
    }

}