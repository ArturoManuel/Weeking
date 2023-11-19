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
import android.widget.TextView;

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

    private AdaptadorPrin adaptadorPrincipal;
    private AdaptadorPrin adaptadorPorLikes;


    public mainFragmento() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        adaptadorPrincipal = new AdaptadorPrin(new ArrayList<>(), getContext());
        adaptadorPorLikes = new AdaptadorPrin(new ArrayList<>(), getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_fragmento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Identificar los TextView de los títulos
        TextView tituloListaPrincipal = view.findViewById(R.id.todosLosEventos);
        TextView tituloListaPorLikes = view.findViewById(R.id.eventosPopulares);

        // Lista principal
        recyclerView = view.findViewById(R.id.lista_prin);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adaptadorPrincipal); // Seteamos el adaptador aquí


        appViewModel.getListaEventos().observe(getViewLifecycleOwner(), eventoClasses -> {
            if (eventoClasses != null && !eventoClasses.isEmpty()) {
                adaptadorPrincipal.updateData(eventoClasses);
                tituloListaPrincipal.setVisibility(View.VISIBLE);
            } else {
                adaptadorPrincipal.updateData(new ArrayList<>()); // Asegúrate de que la lista está vacía si no hay datos
                tituloListaPrincipal.setVisibility(View.GONE);
            }
        });

















        // Lista por likes
        recyclerViewPorLikes = view.findViewById(R.id.lista_populares);
        recyclerViewPorLikes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPorLikes.setAdapter(adaptadorPorLikes); // Seteamos el adaptador aquí

        appViewModel.getEventosByLikes().observe(getViewLifecycleOwner(), eventoClasses -> {
            if (eventoClasses != null && !eventoClasses.isEmpty()) {
                adaptadorPorLikes.updateData(eventoClasses); // Actualizamos datos en vez de crear un nuevo adaptador
                tituloListaPorLikes.setVisibility(View.VISIBLE);
            } else {
                tituloListaPorLikes.setVisibility(View.GONE);
            }
        });
    }





}
