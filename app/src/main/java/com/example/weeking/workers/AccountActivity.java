package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {


    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        TextInputLayout nombre = findViewById(R.id.nombre);
        TextInputLayout correo = findViewById(R.id.correo);
        TextInputLayout estado = findViewById(R.id.estado);
        TextInputEditText nombrea = findViewById(R.id.nombre1);
        TextInputEditText correoa = findViewById(R.id.coreo1);
        TextView estadoa = findViewById(R.id.estados);
        Button cambio = findViewById(R.id.button2);
        setItems(autoCompleteTextView);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("usuarios").whereEqualTo("authUID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.get().addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                QuerySnapshot queryDocumentSnapshot = task.getResult();
                if(!queryDocumentSnapshot.isEmpty()){
                    DocumentSnapshot document = queryDocumentSnapshot.getDocuments().get(0);
                    String nombre1 = document.getString("nombre");
                    nombrea.setText(nombre1);
                    String correo1 = document.getString("correo");
                    correoa.setText(correo1);
                    String estado1 = document.getString("estado");
                    estado.setHint(estado1);
                }}});

          cambio.setOnClickListener(view -> {
              Map<String, Object> datos = new HashMap<>();
              if(!String.valueOf(nombrea.getText()).isEmpty()){
                  datos.put("nombre", String.valueOf(nombrea.getText()));
              }
              if(!String.valueOf(correoa.getText()).isEmpty()){
                  datos.put("correo", String.valueOf(correoa.getText()));
              }
              if(!String.valueOf(estadoa.getText()).isEmpty()){
                  datos.put("estado", String.valueOf(estadoa.getText()));
              }
              Query query1 = db.collection("usuarios").whereEqualTo("authUID", FirebaseAuth.getInstance().getCurrentUser().getUid());
              query1.get().addOnCompleteListener(task1 ->{
                  if(task1.isSuccessful()){
                      QuerySnapshot queryDocumentSnapshot1 = task1.getResult();
                      if(!queryDocumentSnapshot1.isEmpty()){
                          DocumentSnapshot document1 = queryDocumentSnapshot1.getDocuments().get(0);
                          String codigo = document1.getString("codigo");
                          db.collection("usuarios").document(codigo)
                                  .update(datos)
                                  .addOnSuccessListener(documentReference -> {
                                      Toast.makeText(getApplicationContext(), "Se actualizo los datos con exito", Toast.LENGTH_SHORT).show();

                                  })
                                  .addOnFailureListener(e -> {
                                  });}}});
          });
    }

    private void setItems( AutoCompleteTextView textView){
        textView = findViewById(R.id.estados);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String[] items ={"Egresado"};
        String[] items1 ={"Egresado","Estudiante"};
        Query query = db.collection("usuarios").whereEqualTo("authUID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        AutoCompleteTextView finalTextView = textView;
        query.get().addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                QuerySnapshot queryDocumentSnapshot = task.getResult();
                if(!queryDocumentSnapshot.isEmpty()){
                    DocumentSnapshot document = queryDocumentSnapshot.getDocuments().get(0);
                    String estado1 = document.getString("estado");
                    if(estado1.equals("Estudiante")){
                        adapter = new ArrayAdapter<String>(this,R.layout.linear_items,items1);
                        finalTextView.setAdapter(adapter);
                    }else {
                        adapter = new ArrayAdapter<String>(this,R.layout.linear_items,items);
                        finalTextView.setAdapter(adapter);
                    }
                }}});

    }
}