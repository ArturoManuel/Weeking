package com.example.weeking.workers;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.weeking.Adapter.AdaptadorAlu;
import com.example.weeking.Adapter.AdaptadorDon;
import com.example.weeking.R;
import com.example.weeking.entity.ListaDon;
import com.example.weeking.entity.Usuario;
import com.example.weeking.workers.viewModels.AppViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Lista_don extends AppCompatActivity {
    List<ListaDon> donadores = new ArrayList<>();
    List<Usuario> alun = new ArrayList<>();
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_don);
        AppViewModel appViewModel= new ViewModelProvider(this).get(AppViewModel.class);
        db = FirebaseFirestore.getInstance();
        db.collection("donaciones").addSnapshotListener((collection, error) -> {
            if (error != null) {
                Log.d("lectura", "Error listening for document changes.");
                return;
            }
            if (collection != null && !collection.isEmpty()) {
                for (QueryDocumentSnapshot document : collection) {
                    ListaDon dona = document.toObject(ListaDon.class);
                    donadores.add(dona);
                }
                appViewModel.getListaDona().postValue(donadores);
            }
        });
        db.collection("usuarios").addSnapshotListener((collection, error) -> {
            if (error != null) {
                Log.d("lectura", "Error listening for document changes.");
                return;
            }
            if (collection != null && !collection.isEmpty()) {
                for (QueryDocumentSnapshot document : collection) {
                    Usuario usuario = document.toObject(Usuario.class);
                    alun.add(usuario);
                }

            }
        });
        if(donadores !=null){
            Log.d("lectura", "adfghd");
        }
        AdaptadorDon listaAdapter = new AdaptadorDon(donadores,this);
        RecyclerView recyclerView = findViewById(R.id.lista_don);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listaAdapter);
        AdaptadorAlu listAdapter = new AdaptadorAlu(alun,this);
        RecyclerView recyclerView1 = findViewById(R.id.lista_alu);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(listAdapter);
        TextView titulo = findViewById(R.id.textView52);
        TextView donaci = findViewById(R.id.textView19);
        TextView alu = findViewById(R.id.textView22);
        donaci.setOnClickListener(view -> {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView1.setVisibility(View.GONE);
            donaci.setAlpha(1F);
            alu.setAlpha(0.4F);
            titulo.setText("Lista de Donaciones");
        });
        alu.setOnClickListener(view -> {
            recyclerView1.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            alu.setAlpha(1F);
            donaci.setAlpha(0.4F);
            titulo.setText("Lista de Alumnos");
        });
    }
}