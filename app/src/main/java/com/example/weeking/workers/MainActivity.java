package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.weeking.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.iniciarSesion.setOnClickListener(v -> navigateToActivity(VistaPrincipal.class));
        binding.recuperarContrasena.setOnClickListener(v-> navigateToActivity(ContrasenaRecuperacion_Activity.class));
        binding.registrate.setOnClickListener(v->navigateToActivity(RegistroActivity.class));
        binding.imageView.setOnClickListener(v -> navigateToActivity(ActividadesActivity.class));
        binding.bienvenidos.setOnClickListener(v -> navigateToActivity(Lista_don.class));
    }

    private void navigateToActivity(Class<?> destinationClass) {
        Intent intent = new Intent(MainActivity.this, destinationClass);
        startActivity(intent);
    }

}