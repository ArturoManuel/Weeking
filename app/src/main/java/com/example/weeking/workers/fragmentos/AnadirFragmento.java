package com.example.weeking.workers.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weeking.Adapter.AlumnoAdapter;
import com.example.weeking.R;
import com.example.weeking.entity.Alumno;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AnadirFragmento extends Fragment {

    private RecyclerView recyclerViewAlumnos;
    private AlumnoAdapter alumnoAdapter;
    private List<Alumno> listaAlumnos = new ArrayList<>();;

    private FirebaseFirestore db;

    private String idActividad;

    public AnadirFragmento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anadir_fragmento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar el RecyclerView
        recyclerViewAlumnos = view.findViewById(R.id.recyclerViewAlumnos);  // Asume que tu RecyclerView en el XML tiene el ID "recyclerViewAlumnos"
        recyclerViewAlumnos.setLayoutManager(new LinearLayoutManager(getContext()));
        // Inicializar la lista de alumnos
        if (getArguments() != null) {
            idActividad = getArguments().getString("idActividad");
            if (idActividad != null) {
                Log.d("fragmento", idActividad);
            } else {
                Log.d("fragmento", "variableConElMensaje es null");
            }
        }
        consultarAlumnosDesdeFirestore();
    }

    private void consultarAlumnosDesdeFirestore() {
        db.collection("usuarios")
                .whereNotEqualTo("rol", "administrador")
                // El listener reaccionará a los cambios en tiempo real
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("AnadirFragmento", "Listen failed.", e);
                            return;
                        }

                        List<Alumno> alumnosFiltrados = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            // Verificar si el campo "activity" existe y si contiene el idActividad
                            List<String> actividades = (List<String>) document.get("activity");
                            if (actividades == null || !actividades.contains(idActividad)) {
                                String nombre = document.getString("nombre");
                                String rol = document.getString("rol");
                                String codigo = document.getString("codigo");
                                Alumno alumno = new Alumno(nombre, rol, codigo);
                                alumnosFiltrados.add(alumno);
                            }
                        }

                        // Establecer el adaptador en el RecyclerView con los alumnos filtrados
                        if (getContext() != null) {
                            alumnoAdapter = new AlumnoAdapter(getContext(), alumnosFiltrados, idActividad);
                            recyclerViewAlumnos.setAdapter(alumnoAdapter);
                        }
                    }
                });
    }




}

