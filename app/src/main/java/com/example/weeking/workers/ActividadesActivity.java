package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weeking.Adapter.Adaptador;
import com.example.weeking.R;
import com.example.weeking.databinding.ActivityMainBinding;
import com.example.weeking.entity.LIstaAct;

import java.util.ArrayList;
import java.util.List;
public class ActividadesActivity extends AppCompatActivity {

    List<LIstaAct> elements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);
        init();

        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
        Button agregar = findViewById(R.id.btnAddActivity);
        agregar.setOnClickListener(v -> {
            Intent intent = new Intent(ActividadesActivity.this,NuevoEventoActivity.class);
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
}