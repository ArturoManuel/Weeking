package com.example.weeking.workers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputEditText;
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
        /*binding.codigoPucp.addTextChangedListener(createTextWatcher(binding.codigoPucp));
        binding.nombre.addTextChangedListener(createTextWatcher(binding.nombre));
        binding.apellido.addTextChangedListener(createTextWatcher(binding.apellido));
        binding.correo.addTextChangedListener(createTextWatcher(binding.correo));
        binding.contrasena1.addTextChangedListener(createTextWatcher(binding.contrasena1));
        binding.contrasena2.addTextChangedListener(createTextWatcher(binding.contrasena2));*/

        binding.tienescuenta.setOnClickListener(v -> navigateToActivity(MainActivity.class));

        binding.btnRegister.setOnClickListener(v -> {
            // Obtener valores de los campos
            String codigo = binding.codigoPucp.getText().toString();
            String nombre = binding.nombre.getText().toString();
            String apellido = binding.apellido.getText().toString();
            String correo = binding.correo.getText().toString();
            String contrasena = binding.contrasena1.getText().toString();
            String contrasena2 = binding.contrasena2.getText().toString();
            String estado = binding.estados.getText().toString();

            // Validaciones
            if (codigo.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || contrasena2.isEmpty() || estado.isEmpty()) {
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
            checkEmailAndAlumnoCodeUnique(correo, codigo);

        });


    }
    private TextWatcher createTextWatcher(final TextInputEditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // No es necesario realizar acciones antes de que el texto cambie
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Remueve espacios y subrayados del texto
                String formattedText = charSequence.toString().replaceAll("[\\s_]", "");

                // Actualiza el texto del EditText sin espacios ni subrayados
                editText.removeTextChangedListener(this);
                editText.setText(formattedText);
                editText.setSelection(formattedText.length());
                editText.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No es necesario realizar acciones después de que el texto cambie
            }
        };
    }
    private boolean isValidAlumnoCode(String codigo) {
        // Verifica si el código de alumno es numérico y tiene 8 dígitos
        return codigo.matches("\\d{8}");
    }
    private boolean isValidEmail(String email) {
        // Utilizar una expresión regular para validar el formato del correo electrónico
        String emailPattern = "[a-zA-Z0-9._%+-]+@(gmail\\.com|pucp\\.edu\\.pe|pucp\\.pe)";
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
                        user.put("nombre", binding.nombre.getText().toString() + " " + binding.apellido.getText().toString());
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
                                    Toast.makeText(this, "Usuario registrado con éxito, espere que lo verifique", Toast.LENGTH_SHORT).show();
                                    Map<String, Object> map1 = new HashMap<>();
                                    map1.put("notifi","Se ha registrado "+binding.nombre.getText().toString() + " " + binding.apellido.getText().toString()+". Proceder a su verificación");
                                    map1.put("codigo","20190411");
                                    db.collection("noti").add(map1);
                                    navigateToActivity(MainActivity.class);
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(RegistroActivity.this, "SignUp Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void navigateToActivity(Class<?> destinationClass) {
        Intent intent = new Intent(RegistroActivity.this, destinationClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    private void setItems( AutoCompleteTextView textView){
        textView = findViewById(R.id.estados);
        adapter = new ArrayAdapter<String>(this,R.layout.linear_items,items);
        textView.setAdapter(adapter);
    }


}