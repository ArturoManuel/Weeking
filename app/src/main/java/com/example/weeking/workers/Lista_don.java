package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.weeking.Adapter.Adaptador;
import com.example.weeking.Adapter.AdaptadorDon;
import com.example.weeking.R;
import com.example.weeking.entity.LIstaAct;
import com.example.weeking.entity.ListaDon;

import java.util.ArrayList;
import java.util.List;

public class Lista_don extends AppCompatActivity {
    List<ListaDon> elements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_don);
        init();
        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
    }

    public void init(){
        elements = new ArrayList<>();
        elements.add(new ListaDon("Jorge Guillem",230,true,"20190234"));
        elements.add(new ListaDon("Javie Espinoza",330,true,"20191234"));
        elements.add(new ListaDon("Manuel Carrera",50,false,"20200234"));
        elements.add(new ListaDon("Cesar Acuna",230,true,"20210234"));
        elements.add(new ListaDon("Jose Peralta",350,false,"201491234"));
        elements.add(new ListaDon("Andres Carlos",30,true,"20180334"));
        elements.add(new ListaDon("Jorge Guillem",230,true,"20190234"));
        elements.add(new ListaDon("Jorge Guillem",230,true,"20190234"));
        elements.add(new ListaDon("Jorge Guillem",230,true,"20190234"));

        AdaptadorDon listaAdapter = new AdaptadorDon(elements,this);
        RecyclerView recyclerView = findViewById(R.id.lista_don);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listaAdapter);
    }
}