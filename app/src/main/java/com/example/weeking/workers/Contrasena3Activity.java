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
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Cambio de contraseña");
        }
        contrasenaLayout = findViewById(R.id.contraseña);
        contrasenaText = findViewById(R.id.contraseñatext);

        contrasena2Layout = findViewById(R.id.contraseña2);
        contrasena2Text = findViewById(R.id.contraseñatext2);

        Button btnContinuar = findViewById(R.id.btnRegister);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validarContrasenas();
            }
        });

    }

    private void validarContrasenas() {
        String contrasena1 = contrasenaText.getText().toString().trim();
        String contrasena2 = contrasena2Text.getText().toString().trim();

        if (contrasena1.isEmpty() || contrasena2.isEmpty()) {

            contrasenaLayout.setError("Completa ambos campos");
            contrasena2Layout.setError("Completa ambos campos");
        } else if (!contrasena1.equals(contrasena2)) {

            contrasenaLayout.setError("Las contraseñas no coinciden");
            contrasena2Layout.setError("Las contraseñas no coinciden");
        } else {

            cambiarContrasena(contrasena1);
        }
    }
    private void cambiarContrasena(String nuevaContrasena) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {


            user.updatePassword(nuevaContrasena)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            Toast.makeText(this, "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(this, "Error al cambiar la contraseña: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {

            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}