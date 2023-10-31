package com.example.weeking.workers;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weeking.Adapter.Adaptador;
import com.example.weeking.R;
import com.example.weeking.databinding.ActivityActividadesBinding;
import com.example.weeking.databinding.ActivityMainBinding;
import com.example.weeking.entity.Actividad;
import com.example.weeking.entity.LIstaAct;
import com.example.weeking.workers.fragmentos.listaFragmento;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class ActividadesActivity extends AppCompatActivity {
    ActivityActividadesBinding binding;
    List<LIstaAct> elements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActividadesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();






        binding.btnAddActivity.setOnClickListener(v -> {
            List<String> listaEventosIds = Arrays.asList("");
//            crearNuevaActividad("Nombre de la actividad", listaEventosIds);
//            Log.d("mensaje","Se creo una actividad en firestorage");
            Intent intent =  new Intent(ActividadesActivity.this, NuevaActividadActivity.class);
            startActivity(intent);


        });
    }

    public void init(){
        elements = new ArrayList<>();
        elements.add(new LIstaAct("Dota","a"));
        elements.add(new LIstaAct("LOL","a"));
        elements.add(new LIstaAct("Futbol","a"));
        elements.add(new LIstaAct("Voley","a"));
        elements.add(new LIstaAct("Basquet","a"));
        elements.add(new LIstaAct("Ajedrez","a"));
        Adaptador listaAdapter = new Adaptador(elements,this);
        RecyclerView recyclerView = findViewById(R.id.lista_act_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listaAdapter);
    }

    private void crearNuevaActividad(String nombre, List<String> listaEventosIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference actividadRef = db.collection("activity");

        // Crear un nuevo objeto de actividad
        Actividad actividad = new Actividad();
        actividad.setNombre(nombre);
        actividad.setListaEventosIds(listaEventosIds);

        // Agregar la actividad a Firestore
        actividadRef.add(actividad)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Documento añadido con ID: " + documentReference.getId());

                        // Actualizar el ID en el objeto actividad y en Firestore
                        actividad.setId(documentReference.getId());
                        actividadRef.document(documentReference.getId()).set(actividad);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error añadiendo el documento", e);
                    }
                });
    }

}