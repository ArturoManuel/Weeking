package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.R;
import com.example.weeking.entity.ListaDon;
import com.example.weeking.entity.Usuario;
import com.example.weeking.workers.viewModels.AppViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class StatusActivity extends AppCompatActivity {
    FirebaseFirestore db;
    private Button btnAdquiereKit;
    private Button btnListaDonaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        db = FirebaseFirestore.getInstance();
        ImageView imagen = findViewById(R.id.imageView4);
        TextView monto = findViewById(R.id.Donacion);
        TextView motivo = findViewById(R.id.Donacion1);
        final String[] codigo = {null};
        btnAdquiereKit = findViewById(R.id.btnKitAccess);
        Query query = db.collection("usuarios").whereEqualTo("authUID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot queryDocumentSnapshot = task.getResult();
                if (!queryDocumentSnapshot.isEmpty()) {
                    DocumentSnapshot document = queryDocumentSnapshot.getDocuments().get(0);
                    Query query1 = db.collection("donaciones").whereEqualTo("codigo", document.getString("codigo"));
                    query1.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            QuerySnapshot queryDocumentSnapshot1 = task1.getResult();
                            if (!queryDocumentSnapshot1.isEmpty()) {
                                DocumentSnapshot document1 = queryDocumentSnapshot1.getDocuments().get(0);
                                if (document1.getData() != null) {
                                    ListaDon dona = document1.toObject(ListaDon.class);
                                    monto.setText(dona.getMonto() + " soles");
                                    codigo[0] = dona.getCodigo();
                                    if (dona.getMonto() >= 200 && dona.isEgresado()) {
                                        btnAdquiereKit.setVisibility(View.VISIBLE);
                                    }
                                    String rechazo = document1.getString("rechazo");
                                    if (rechazo.equals("1")) {
                                        Picasso.get().load(dona.getFoto()).into(imagen);
                                    } else if (rechazo.equals("0")) {
                                        motivo.setText("Sin verificaciones pendientes");
                                        imagen.setVisibility(View.GONE);
                                    } else {
                                        imagen.setVisibility(View.GONE);
                                        motivo.setText("Ultima Donacion ha sido rechazado por esto:\n" + rechazo);
                                    }
                                } else {
                                    motivo.setText("No has realizado ninguna donacion");
                                    imagen.setVisibility(View.GONE);
                                }

                            }
                        }
                    });
                }
            }
        });

        btnAdquiereKit.setOnClickListener(view -> {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("notifi","El alumno con codigo "+ codigo[0] +" ha solicitado un kit");
            map1.put("codigo","20190411");
            db.collection("noti").add(map1);
            Map<String, Object> map = new HashMap<>();
            map.put("egresado",false);
            db.collection("donaciones").document(codigo[0]).update(map);
            finish();
        });


    }}