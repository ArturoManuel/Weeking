package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class Pago extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);
        db = FirebaseFirestore.getInstance();

        Button comprobante = findViewById(R.id.button11);
        comprobante.setOnClickListener(v -> {
            /*db.collection("usuarios").document(codigo).set(user)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());*/
        });
    }
}