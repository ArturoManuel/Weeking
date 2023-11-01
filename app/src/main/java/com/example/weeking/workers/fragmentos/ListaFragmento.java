package com.example.weeking.workers.fragmentos;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import com.example.weeking.Adapter.EventosAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.example.weeking.R;
import com.example.weeking.entity.Actividad;
import com.example.weeking.entity.EventoClass;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaFragmento extends Fragment {
    private RecyclerView recyclerView;
    private EventosAdapter adapter;  // Cambiado de RecyclerView.Adapter a EventosAdapter
    private List<EventoClass> elements;  // Cambiado de List<ListaEven> a List<EventoClass>

    public ListaFragmento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_fragmento, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        elements = generarDatosFicticios();  // Usa el método para obtener datos ficticios
        adapter = new EventosAdapter(elements, getActivity()); // Usando EventosAdapter
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<EventoClass> generarDatosFicticios() {
        List<EventoClass> listaDeEventos = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            EventoClass evento = new EventoClass(
                    "Descripción del evento " + i,
                    true,
                    new Timestamp(new Date()), // Fecha actual
                    "URL_ficticia_de_imagen_" + i,
                    i,
                    "Nombre del evento " + i,
                    "Ubicación " + i,
                    "ID_evento_" + i
            );
            listaDeEventos.add(evento);
        }

        return listaDeEventos;
    }



}

