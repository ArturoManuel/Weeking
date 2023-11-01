package com.example.weeking.workers.fragmentos;
import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import com.example.weeking.Adapter.EventosAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weeking.R;
import com.example.weeking.entity.Actividad;
import com.example.weeking.entity.EventoClass;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaFragmento extends Fragment {
    private RecyclerView recyclerView;
    private EventosAdapter adapter;  // Cambiado de RecyclerView.Adapter a EventosAdapter
    private List<EventoClass> elements;  // Cambiado de List<ListaEven> a List<EventoClass>

    private FirebaseFirestore db;
    private String idActividad;  // Este es el ID de la actividad para la que deseas obtener los eventos


    public ListaFragmento() {
        // Required empty public constructor
    }

    public ListaFragmento(String idActividad) {
        this.idActividad = idActividad;
        db = FirebaseFirestore.getInstance();
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
       obtenerEventosDeFirestore();;  // Usa el método para obtener datos ficticios

        return view;
    }


    private void obtenerEventosDeFirestore() {
        CollectionReference eventosRef = db.collection("Eventos");

        // Suponiendo que los eventos tienen un campo 'idActividad' que indica a qué actividad pertenecen
        eventosRef.whereEqualTo("idActividad", idActividad)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<EventoClass> listaDeEventos = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        EventoClass evento = document.toObject(EventoClass.class);
                        listaDeEventos.add(evento);
                    }
                    // Actualiza el adaptador con los eventos obtenidos
                    adapter = new EventosAdapter(listaDeEventos, getActivity());
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error obteniendo eventos", e);
                    Toast.makeText(getActivity(), "No se pudo obtener eventos", Toast.LENGTH_SHORT).show();
                });
    }




}

