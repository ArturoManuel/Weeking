package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ContrasenaRecuperacion_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrasena_recuperacion);

        Button button = findViewById(R.id.btnContinuar);
        TextInputEditText correo = findViewById(R.id.correo);
        button.setOnClickListener(view -> {
            String corre = String.valueOf(correo.getText());

            if(corre.isEmpty()){
                Toast.makeText(this, "Correo vacio", Toast.LENGTH_SHORT).show();
            }else{
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(corre)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ContrasenaRecuperacion_Activity.this, "reestablecimiento enviado a su correo", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ContrasenaRecuperacion_Activity.this, "Correo no registrado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }
}