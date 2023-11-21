package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Don_ve extends AppCompatActivity {
    FirebaseFirestore db;
    String codigo,nombre,rechazo_don;
    Integer donacion;
    Boolean egresado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_don_ve);

        TextInputEditText monto = findViewById(R.id.input);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            codigo = extras.getString("codigo");
            donacion = extras.getInt("dona");
            nombre = extras.getString("nombre");
            rechazo_don = extras.getString("recha");
            egresado = extras.getBoolean("egre");
        }

        Button confirmar = findViewById(R.id.button15);
        confirmar.setOnClickListener(v -> {
            donacion = donacion + Integer.parseInt(String.valueOf(monto.getText()));
            Map<String, Object> map = new HashMap<>();
            map.put("monto",donacion);
            map.put("rechazo","0");
            Log.d("monto", String.valueOf(donacion));
            db.collection("donaciones").document(codigo).update(map);
            Intent intent1 = new Intent(this, Lista_don.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent1);
            finish();
        });
    }
}