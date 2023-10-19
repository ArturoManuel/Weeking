package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.example.weeking.Adapter.AdaptadorE;
import com.example.weeking.R;
import com.example.weeking.databinding.ActivityListchatsBinding;
import com.example.weeking.entity.ListaEven;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Listchats extends AppCompatActivity {

    Intent intent;
    List<ListaEven> elements;
    ActivityListchatsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listchats);

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
        RecyclerView recyclerView = findViewById(R.id.lista_chat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listaAdapter);
    }


}