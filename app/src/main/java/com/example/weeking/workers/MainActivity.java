package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.weeking.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflar el layout aquí antes de llamar a setContentView()
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();


        binding.iniciarSesion.setOnClickListener(v -> {
            String correo = binding.correo.getText().toString();
            String contrasena = binding.password.getText().toString();

            if (!correo.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                if (!contrasena.isEmpty()) {
                    auth.signInWithEmailAndPassword(correo, contrasena)
                            .addOnSuccessListener(authResult -> {
                                Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, VistaPrincipal.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(MainActivity.this, "Error en el inicio de sesión: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    binding.password.setError("No se permiten campos vacíos");
                }
            } else if (correo.isEmpty()) {
                binding.correo.setError("No se permiten campos vacíos");
            } else {
                binding.correo.setError("Por favor, introduce un correo electrónico válido");
            }
        });


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