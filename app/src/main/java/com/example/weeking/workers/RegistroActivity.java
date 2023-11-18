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
import java.util.Objects;

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
            if (!isValidAlumnoCode(codigo)) {
                Toast.makeText(this, "El código de alumno debe ser numérico y tener 8 dígitos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidEmail(correo)) {
                Toast.makeText(this, "Formato de correo electrónico no válido", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("msg-erroe","acá");
            // Crear usuario con correo y contraseña en Firebase Authentication
            /*auth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("msg-error","Se envía");
                    String authUID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                    // Crear un objeto Map para almacenar los valores en Firestore
                    Map<String, Object> user = new HashMap<>();
                    user.put("codigo", codigo);
                    user.put("nombre", nombre);
                    user.put("correo", correo);
                    user.put("estado", estado);
                    user.put("rol","alumno");
                    user.put("apoyo","no_apoya");
                    user.put("imagen_url", "tu_url_por_defecto_aqui");
                    user.put("authUID", authUID);


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
       */
            checkEmailAndAlumnoCodeUnique(correo, codigo);
        });


    }
    private boolean isValidAlumnoCode(String codigo) {
        // Verifica si el código de alumno es numérico y tiene 8 dígitos
        return codigo.matches("\\d{8}");
    }
    private boolean isValidEmail(String email) {
        // Utilizar una expresión regular para validar el formato del correo electrónico
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
    private void checkEmailAndAlumnoCodeUnique(String email, String codigoAlumno) {
        // Verifica si el correo electrónico ya está en uso
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getSignInMethods() != null && task.getResult().getSignInMethods().size() > 0) {
                        // El correo electrónico ya está en uso
                        Toast.makeText(this, "El correo electrónico ya está en uso", Toast.LENGTH_SHORT).show();
                    } else {
                        // El correo electrónico es único, ahora verifica el código de alumno
                        db.collection("usuarios").document(codigoAlumno).get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful() && task1.getResult() != null && task1.getResult().exists()) {
                                        // El código de alumno ya está en uso
                                        Toast.makeText(this, "El código de alumno ya está en uso", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Ambos son únicos, procede con el registro
                                        registerUser(email, codigoAlumno);
                                    }
                                });
                    }
                });
    }
    private void registerUser(String email, String codigoAlumno) {
        auth.createUserWithEmailAndPassword(email, binding.contrasena1.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String authUID = Objects.requireNonNull(auth.getCurrentUser()).getUid();

                        // Crear un objeto Map para almacenar los valores en Firestore
                        Map<String, Object> user = new HashMap<>();
                        user.put("codigo", codigoAlumno);
                        user.put("nombre", binding.nombre.getText().toString());
                        user.put("correo", email);
                        user.put("estado", binding.estados.getText().toString());
                        user.put("rol", "alumno");
                        user.put("apoyo", "no_apoya");
                        user.put("imagen_url", "https://firebasestorage.googleapis.com/v0/b/weeking-c2d7c.appspot.com/o/usuarios%2Fanonimo.png?alt=media&token=1eb0b99e-0023-47ce-924c-c0f9846ead19");
                        user.put("authUID", authUID);
                        user.put("ban","0");

                        // Añadir datos en Firestore
                        db.collection("usuarios").document(codigoAlumno).set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                                    navigateToActivity(VistaPrincipal.class);
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(RegistroActivity.this, "SignUp Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
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