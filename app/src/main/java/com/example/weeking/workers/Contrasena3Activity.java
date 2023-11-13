package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Contrasena3Activity extends AppCompatActivity {
    TextInputLayout contrasenaLayout;
    TextInputLayout contrasena2Layout;
    TextInputEditText contrasenaText;
    TextInputEditText contrasena2Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrasena3);

        contrasenaLayout = findViewById(R.id.contraseña);
        contrasenaText = findViewById(R.id.contraseñatext);

        contrasena2Layout = findViewById(R.id.contraseña2);
        contrasena2Text = findViewById(R.id.contraseñatext2);

        Button btnContinuar = findViewById(R.id.btnRegister);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Llamada a la función de validación
                validarContrasenas();
            }
        });

    }

    private void validarContrasenas() {
        String contrasena1 = contrasenaText.getText().toString().trim();
        String contrasena2 = contrasena2Text.getText().toString().trim();

        if (contrasena1.isEmpty() || contrasena2.isEmpty()) {
            // Mostrar un mensaje de error si algún campo está vacío
            contrasenaLayout.setError("Completa ambos campos");
            contrasena2Layout.setError("Completa ambos campos");
        } else if (!contrasena1.equals(contrasena2)) {
            // Mostrar un mensaje de error si las contraseñas no coinciden
            contrasenaLayout.setError("Las contraseñas no coinciden");
            contrasena2Layout.setError("Las contraseñas no coinciden");
        } else {
            // Las contraseñas coinciden, puedes continuar con la lógica de cambio de contraseña
            // ... (agrega tu lógica aquí)
            // Por ejemplo, puedes llamar a una función para cambiar la contraseña en tu sistema
            cambiarContrasena(contrasena1);
        }
    }
    private void cambiarContrasena(String nuevaContrasena) {
        // Obtén el usuario actualmente autenticado
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Aquí puedes abrir un diálogo o una nueva actividad para que el usuario ingrese su nueva contraseña
            // Después de obtener la nueva contraseña, utiliza el siguiente código para cambiarla

            user.updatePassword(nuevaContrasena)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Contraseña cambiada con éxito
                            Toast.makeText(this, "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            // Ocurrió un error al cambiar la contraseña
                            Toast.makeText(this, "Error al cambiar la contraseña: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // El usuario no está autenticado, maneja este caso según tus necesidades
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}