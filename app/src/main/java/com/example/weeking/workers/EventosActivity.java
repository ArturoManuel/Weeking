package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.weeking.Adapter.AdaptadorE;
import com.example.weeking.R;
import com.example.weeking.entity.ListaEven;

import java.util.ArrayList;
import java.util.List;

public class EventosActivity extends AppCompatActivity {


    List<ListaEven> elements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        Button btnAddEvent =  findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(v -> {
            Intent intent =  new Intent(EventosActivity.this, ActividadActivity.class);
            startActivity(intent);
            finish();
        });
        init();


    }

    public void init(){
        elements = new ArrayList<>();
        elements.add(new ListaEven("Torneo de ajedrez","a","en dos dias"));
        elements.add(new ListaEven("Torneo de Futsal","a","finalizado"));
        elements.add(new ListaEven("Torneo de Basquet","a","en un dia"));
        elements.add(new ListaEven("Torneo de Voley","a","en proceso"));
        elements.add(new ListaEven("Torneo de LOL","a","en dos dias"));
        elements.add(new ListaEven("Torneo de Dota","a","en dos dias"));
        elements.add(new ListaEven("Torneo de BIKAS","a","en dos dias"));
        AdaptadorE listaAdapter = new AdaptadorE(elements,this);
        RecyclerView recyclerView = findViewById(R.id.listareEven);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listaAdapter);
    }
}