package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

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
        btnListaDonaciones = findViewById(R.id.btnListaDonar);
        btnAdquiereKit = findViewById(R.id.btnKitAccess);
        Query query = db.collection("usuarios").whereEqualTo("authUID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.get().addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                QuerySnapshot queryDocumentSnapshot = task.getResult();
                if(!queryDocumentSnapshot.isEmpty()){
                    DocumentSnapshot document = queryDocumentSnapshot.getDocuments().get(0);
                    Log.d("aaaaa","adas");
                    Query query1 = db.collection("donaciones").whereEqualTo("codigo", document.getString("codigo"));
                    query1.get().addOnCompleteListener(task1 ->{
                        if(task1.isSuccessful()){
                            QuerySnapshot queryDocumentSnapshot1 = task1.getResult();
                            Log.d("aaaaa","adas");
                            if(!queryDocumentSnapshot.isEmpty()){
                                Log.d("aaaaa","adas1");
                                DocumentSnapshot document1 = queryDocumentSnapshot1.getDocuments().get(0);
                                if(document1.getData() != null){
                                    Log.d("aaaaa","adas2");
                                    ListaDon dona = document1.toObject(ListaDon.class);
                                    Log.d("aaaaa","adas3");
                                monto.setText(dona.getMonto()+" soles");
                                //Verifica para que se active el boton de adquirir kit (falta relacionarlo con los egresados)
                                if(dona.getMonto() >= 200){
                                    btnAdquiereKit.setVisibility(View.VISIBLE);
                                } else {
                                    btnAdquiereKit.setVisibility(View.INVISIBLE);
                                }
                                String rechazo = document1.getString("rechazo");
                                if(rechazo.equals("1")){
                                    Picasso.get().load(dona.getFoto()).into(imagen);
                                } else if (rechazo.equals("0")) {
                                    motivo.setText("Sin verificaciones pendientes");
                                    imagen.setVisibility(View.GONE);
                                } else {
                                    imagen.setVisibility(View.GONE);
                                    motivo.setText("Ultima Donacion ha sido rechazado por esto:\n"+rechazo);
                                }
                                }else {
                                    Log.d("aaaaa","adas5");
                                    motivo.setText("No has realizado ninguna donacion");
                                    imagen.setVisibility(View.GONE);
                                }};}});}}});
        ///para acceder a la lista de donaciones
        btnListaDonaciones.setOnClickListener(view -> {
            Intent intent = new Intent(StatusActivity.this, ListaDonacionesActivity.class);
            startActivity(intent);
        });
    }

}