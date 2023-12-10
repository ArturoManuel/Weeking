package com.example.weeking.workers.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weeking.Adapter.ComentarioApoyoAdapter;
import com.example.weeking.R;
import com.example.weeking.entity.ComentarioDeApoyo;
import com.example.weeking.entity.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ListarApoyosFragmento extends Fragment {

    private RecyclerView recyclerView;
    private ComentarioApoyoAdapter adapter;
    private List<Usuario> listaAlumnos;

    private static final String ARG_EVENTO_ID = "eventoId";

    // Método estático para crear una nueva instancia del fragmento con argumentos
    public static ListarApoyosFragmento newInstance(String eventoId) {
        ListarApoyosFragmento fragment = new ListarApoyosFragmento();
        Bundle args = new Bundle();
        args.putString(ARG_EVENTO_ID, eventoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar_apoyos_fragmento, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewAlumnos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaAlumnos = new ArrayList<>();
        adapter = new ComentarioApoyoAdapter(getContext(), listaAlumnos);
        recyclerView.setAdapter(adapter);

        // Obtener el ID del evento de los argumentos
        if (getArguments() != null) {
            String eventoId = getArguments().getString(ARG_EVENTO_ID);
            // Carga los alumnos con apoyo en proceso y que tienen un comentario para este eventoId
            cargarAlumnosConApoyoEnProceso(eventoId);
        }

        return view;
    }

    private void cargarAlumnosConApoyoEnProceso(String eventoId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .whereEqualTo("apoyo", "en_proceso")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Usuario alumno = document.toObject(Usuario.class);
                        Map<String, ComentarioDeApoyo> comentarios = alumno.getComentariosDeApoyo();

                        if (comentarios != null && comentarios.containsKey(eventoId)) {
                            // Aquí se verifica si el comentario específico está relacionado con el eventoId
                            ComentarioDeApoyo comentario = comentarios.get(eventoId);
                            if (comentario != null) {
                                // Si se encuentra un comentario, se agrega el alumno a la lista
                                listaAlumnos.add(alumno);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("ListarApoyosFragmento", "Error al cargar alumnos", e));
    }

}