package com.example.weeking.workers.fragmentos;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.weeking.workers.NuevoEventoActivity;
import com.example.weeking.workers.viewModels.AppViewModel;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaFragmento extends Fragment implements EventosAdapter.OnEventoListener {
    private RecyclerView recyclerView;
    private ListenerRegistration eventListenerRegistration;
    private EventosAdapter adapter;
    private List<EventoClass> elements = new ArrayList<>();
    private FirebaseFirestore db;
    private String idActividad;

    private  AppViewModel appViewModel;

    public ListaFragmento() {
        // Constructor vacío requerido
    }

    public ListaFragmento(String idActividad) {
        this.idActividad = idActividad;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_fragmento, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        obtenerEventosDeFirestore();
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Observar cambios en la lista de eventos por actividad
        appViewModel.getEventosByActividadId(idActividad).observe(getViewLifecycleOwner(), nuevosEventos -> {
            actualizarDatos(nuevosEventos);
        });
    }



    private void obtenerEventosDeFirestore() {
        CollectionReference eventosRef = db.collection("Eventos");
        eventListenerRegistration = eventosRef.whereEqualTo("idActividad", idActividad)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Error escuchando los eventos", e);
                        Toast.makeText(getActivity(), "No se pudo obtener eventos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<EventoClass> nuevosEventos = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        EventoClass evento = document.toObject(EventoClass.class);
                        evento.setEventId(document.getId());
                        nuevosEventos.add(evento);
                    }
                    actualizarDatos(nuevosEventos);
                });
    }

    private void actualizarDatos(List<EventoClass> nuevosDatos) {
        elements.clear();
        elements.addAll(nuevosDatos);
        if (adapter == null) {
            adapter = new EventosAdapter(elements, getActivity(), this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setListaEventos(elements);
        }
    }

    @Override
    public void onEliminarClicked(int position, String eventoId) {
        if (position != RecyclerView.NO_POSITION && elements != null && position < elements.size()) {
            EventoClass eventoAEliminar = elements.get(position);
            String eventId = eventoAEliminar.getEventId();
            if (eventId == null) {
                Log.e(TAG, "Evento a eliminar con ID nulo");
                return;
            }
            db.collection("Eventos").document(eventId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Evento eliminado: " + eventId);
                        // Llamar a obtenerEventosDeFirestore para recargar la lista
                        obtenerEventosDeFirestore();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error al eliminar evento", e);
                        Toast.makeText(getContext(), "Error al eliminar evento", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e(TAG, "Intento de eliminar un evento en una posición inválida.");
        }
    }


    @Override
    public void onEditarClicked(String eventoId, String actividadId) {
        Intent intent = new Intent(getActivity(), NuevoEventoActivity.class);
        intent.putExtra("id_evento", eventoId);
        intent.putExtra("id_actividad", actividadId);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (eventListenerRegistration != null) {
            eventListenerRegistration.remove();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
       obtenerEventosDeFirestore();
    }


}

