package com.example.weeking.workers.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weeking.Adapter.AdaptadorE;
import com.example.weeking.Adapter.AdaptadorPrin;
import com.example.weeking.R;
import com.example.weeking.entity.ListaEven;

import java.util.ArrayList;
import java.util.List;


public class chatfragmento extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListaEven> elements;

    public chatfragmento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.lista_chat);
        // Configurar el RecyclerView y el adaptador

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        elements = new ArrayList<>();
        elements = new ArrayList<>();
        elements.add(new ListaEven("Torneo de ajedrez","a","en dos dias"));
        elements.add(new ListaEven("Torneo de Futsal","a","finalizado"));
        elements.add(new ListaEven("Torneo de Basquet","a","en un dia"));
        elements.add(new ListaEven("Torneo de Voley","a","en proceso"));
        elements.add(new ListaEven("Torneo de LOL","a","en dos dias"));
        elements.add(new ListaEven("Torneo de Dota","a","en dos dias"));
        elements.add(new ListaEven("Torneo de BIKAS","a","en dos dias"));
        AdaptadorE listaAdapter = new AdaptadorE(elements,getContext()); // Reemplaza MyAdapter con el nombre de tu adaptador
        recyclerView.setAdapter(listaAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatfragmento, container, false);
    }
}