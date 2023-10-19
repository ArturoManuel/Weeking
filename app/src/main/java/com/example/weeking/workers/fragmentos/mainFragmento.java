package com.example.weeking.workers.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weeking.Adapter.AdaptadorPrin;
import com.example.weeking.R;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.EventoDto;
import com.example.weeking.entity.ListaEven;
import com.example.weeking.workers.viewModels.AppViewModel;

import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class mainFragmento extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewPorLikes;
    private RecyclerView.Adapter adapter;

    // Añadir instancia del ViewModel
    private AppViewModel appViewModel;

    public mainFragmento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener la instancia del ViewModel
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_fragmento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.lista_prin);
        // Configurar el RecyclerView y el adaptador
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        // Observar cambios en la lista de eventos
        appViewModel.getListaEventos().observe(getViewLifecycleOwner(), eventoClasses -> {
            // Actualiza tu UI aquí
            if (eventoClasses != null) {
                adapter = new AdaptadorPrin(eventoClasses, getContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        recyclerViewPorLikes=view.findViewById(R.id.lista_populares);
        recyclerViewPorLikes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        appViewModel.getEventosByLikes().observe(getViewLifecycleOwner(), eventoClasses -> {
            if (eventoClasses != null) {
                adapter = new AdaptadorPrin(eventoClasses, getContext());
                recyclerViewPorLikes.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });



    }
}
