package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.weeking.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Don_recha extends AppCompatActivity {
    FirebaseFirestore db;

    String motivo,codigo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_recha);
        db = FirebaseFirestore.getInstance();
        TextInputEditText recha = findViewById(R.id.recha);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            codigo = extras.getString("codigo");
        }
        Button rechazo = findViewById(R.id.button14);
        rechazo.setOnClickListener(v -> {
            motivo = String.valueOf(recha.getText());
            Map<String, Object> map = new HashMap<>();
            map.put("rechazo",motivo);
            db.collection("donaciones").document(codigo).update(map);
            Map<String, Object> map1 = new HashMap<>();
            map1.put("notifi","Su donacion ha sido rechazada por el siguiente motivo: "+motivo);
            map1.put("codigo",codigo);
            db.collection("noti").add(map1);
            Intent intent1 = new Intent(this, Lista_don.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent1);
            finish();
        });
    }
}