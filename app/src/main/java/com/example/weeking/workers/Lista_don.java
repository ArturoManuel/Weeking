package com.example.weeking.workers;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.weeking.Adapter.AdaptadorDon;
import com.example.weeking.R;
import com.example.weeking.entity.ListaDon;
import com.example.weeking.workers.viewModels.AppViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Lista_don extends AppCompatActivity {
    List<ListaDon> donadores = new ArrayList<>();
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
        if(donadores !=null){
            Log.d("lectura", "adfghd");
        }
        AdaptadorDon listaAdapter = new AdaptadorDon(donadores,this);
        RecyclerView recyclerView = findViewById(R.id.lista_don);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listaAdapter);
        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
    }
}