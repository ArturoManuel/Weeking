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
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        // Obtener el ID del evento de los argumentos
        if (getArguments() != null) {
            String eventoId = getArguments().getString(ARG_EVENTO_ID);
            adapter = new ComentarioApoyoAdapter(getContext(), listaAlumnos,eventoId);
            cargarAlumnosConApoyoEnProceso(eventoId);
        }
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void cargarAlumnosConApoyoEnProceso(String eventoId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        tasks.add(db.collection("usuarios").whereEqualTo("apoyo", "en_proceso").get());
        tasks.add(db.collection("usuarios").whereEqualTo("apoyo", "apoya").get());
        tasks.add(db.collection("usuarios").whereEqualTo("apoyo", "denegado").get());

        // Ejecutar todas las consultas y procesar los resultados cuando todas las tareas se completen
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(results -> {
            listaAlumnos.clear(); // Limpiar la lista actual

            // Procesar cada resultado de consulta
            for (Object result : results) {
                QuerySnapshot snapshot = (QuerySnapshot) result;
                for (QueryDocumentSnapshot document : snapshot) {
                    Usuario alumno = document.toObject(Usuario.class);
                    Map<String, ComentarioDeApoyo> comentarios = alumno.getComentariosDeApoyo();
                    if (comentarios != null && comentarios.containsKey(eventoId)) {
                        ComentarioDeApoyo comentario = comentarios.get(eventoId);
                        if (comentario != null) {
                            listaAlumnos.add(alumno);
                        }
                    }
                }
            }

            adapter.notifyDataSetChanged(); // Notificar cambios al adaptador
        }).addOnFailureListener(e -> Log.e("ListarApoyosFragmento", "Error al cargar alumnos", e));
    }


}