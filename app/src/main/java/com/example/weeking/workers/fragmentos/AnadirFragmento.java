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
    private List<Alumno> listaAlumnos;
    private FirebaseFirestore db;
    private String idActividad;
    private String codigoAlumnoSeleccionadoActualmente;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        listaAlumnos = new ArrayList<>();
        if (getArguments() != null) {
            idActividad = getArguments().getString("idActividad");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anadir_fragmento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewAlumnos = view.findViewById(R.id.recyclerViewAlumnos);
        recyclerViewAlumnos.setLayoutManager(new LinearLayoutManager(getContext()));

        alumnoAdapter = new AlumnoAdapter(getContext(), listaAlumnos, idActividad);
        recyclerViewAlumnos.setAdapter(alumnoAdapter);

        consultarAlumnosDesdeFirestore();
    }

    private void consultarAlumnosDesdeFirestore() {
        db.collection("usuarios")
                .whereNotEqualTo("rol", "administrador")
                .whereEqualTo("ban", "1")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("AnadirFragmento", "Listen failed.", e);
                            return;
                        }

                        List<Alumno> nuevosAlumnos = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            String nombre = document.getString("nombre");
                            String rol = document.getString("rol");
                            String codigo = document.getString("codigo");
                            List<String> activities = (List<String>) document.get("activity");

                            Alumno alumno = new Alumno(nombre, rol, codigo, activities);
                            nuevosAlumnos.add(alumno);

                            if (activities != null && activities.contains(idActividad)) {
                                codigoAlumnoSeleccionadoActualmente = codigo;
                            }
                        }

                        listaAlumnos.clear();
                        listaAlumnos.addAll(nuevosAlumnos);
                        alumnoAdapter.notifyDataSetChanged();
                        alumnoAdapter.setCodigoAlumnoSeleccionado(codigoAlumnoSeleccionadoActualmente);
                    }
                });
    }
}
