package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.atomic.AtomicReference;

public class Donacion extends AppCompatActivity {
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donacion);
        db = FirebaseFirestore.getInstance();
        String codigoactual = FirebaseAuth.getInstance().getCurrentUser().getUid();
        RadioButton tranferencia = findViewById(R.id.transferencia);
        RadioButton plin = findViewById(R.id.plin);
        RadioButton yape = findViewById(R.id.yape);
        Button boton = findViewById(R.id.button8);
        TextView nombre = findViewById(R.id.textView5);
        TextView cod = findViewById(R.id.textView6);
        AtomicReference<Boolean> recha = new AtomicReference<>(false);
        Query query = db.collection("usuarios").whereEqualTo("authUID",codigoactual);
        query.get().addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                QuerySnapshot queryDocumentSnapshot = task.getResult();
                if(!queryDocumentSnapshot.isEmpty()){
                    DocumentSnapshot document = queryDocumentSnapshot.getDocuments().get(0);
                    nombre.setText(document.getString("nombre"));
                    cod.setText(document.getString("codigo"));
                    Query query1 = db.collection("donaciones").whereEqualTo("codigo",document.getString("codigo"));
                    query1.get().addOnCompleteListener(task1 ->{
                        if(task.isSuccessful()){
                            QuerySnapshot queryDocumentSnapshot1 = task1.getResult();
                            if(!queryDocumentSnapshot.isEmpty()){
                                DocumentSnapshot document1 = queryDocumentSnapshot1.getDocuments().get(0);
                                if(document1.getString("rechazo").equals("1")){
                                    recha.set(true);
                                };
                            }
                        }
                    } );
                }
            }
        } );
        boton.setOnClickListener(view -> {
            if(recha.get()){
                Toast.makeText(this, "tines una donacion pendiendo de verificar", Toast.LENGTH_SHORT).show();
            }else{
                if (tranferencia.isChecked()) {
                    Intent intent = new Intent(Donacion.this, Transferencia.class);
                    startActivity(intent);
                } else if (plin.isChecked()) {
                    Intent intent = new Intent(Donacion.this, Plin.class);
                    startActivity(intent);
                } else if (yape.isChecked()) {
                    Intent intent = new Intent(Donacion.this, Yape.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "No has seleccionado ninguno", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}