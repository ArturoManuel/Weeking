package com.example.weeking.workers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.R;
import com.example.weeking.databinding.ActivityRegistroBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroActivity extends AppCompatActivity {

    String[] items ={"Egresado","Estudiante"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapter;

    ActivityRegistroBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // función para mostrar los items de estados;
        setItems(autoCompleteTextView);
        db = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

        binding.tienescuenta.setOnClickListener(v -> navigateToActivity(MainActivity.class));

        binding.btnRegister.setOnClickListener(v -> {
            // Obtener valores de los campos
            String codigo = binding.codigoPucp.getText().toString();
            String nombre = binding.nombre.getText().toString();
            String correo = binding.correo.getText().toString();
            String contrasena = binding.contrasena1.getText().toString();
            String contrasena2 = binding.contrasena2.getText().toString();
            String estado = binding.estados.getText().toString();

            // Validaciones
            if (codigo.isEmpty() || nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || contrasena2.isEmpty() || estado.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!contrasena.equals(contrasena2)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("msg-erroe","acá");
            // Crear usuario con correo y contraseña en Firebase Authentication
            auth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("msg-error","Se envía");

                    // Crear un objeto Map para almacenar los valores en Firestore
                    Map<String, Object> user = new HashMap<>();
                    user.put("codigo", codigo);
                    user.put("nombre", nombre);
                    user.put("correo", correo);
                    user.put("contrasena", contrasena); // No almacenes contraseñas en texto claro en una base de datos real
                    user.put("estado", estado);
                    user.put("rol","alumno");
                    user.put("apoyo","no_apoya");
                    user.put("imagen_url", "tu_url_por_defecto_aqui");

                    // Añadir datos en Firestore
                    db.collection("usuarios").document(codigo).set(user)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                                navigateToActivity(VistaPrincipal.class); // Navega a la siguiente actividad solo si el registro es exitoso
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                } else {
                    Log.d("msg","no sale");
                    Toast.makeText(RegistroActivity.this, "SignUp Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


    }

    private void navigateToActivity(Class<?> destinationClass) {
        Intent intent = new Intent(RegistroActivity.this, destinationClass);
        startActivity(intent);
        finish();
    }

    private void setItems( AutoCompleteTextView textView){
        textView = findViewById(R.id.estados);
        adapter = new ArrayAdapter<String>(this,R.layout.linear_items,items);
        textView.setAdapter(adapter);
    }


}