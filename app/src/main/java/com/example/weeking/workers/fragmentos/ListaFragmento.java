package com.example.weeking.workers.fragmentos;
import static android.content.ContentValues.TAG;

import android.content.Intent;
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
import com.example.weeking.workers.NuevoEventoActivity;
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
    public void onStart() {
        super.onStart();
        obtenerEventosDeFirestore();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Remueve el listener cuando el fragmento ya no es visible
        if (eventListenerRegistration != null) {
            eventListenerRegistration.remove();
        }
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

        // Adjunta el listener y guarda la referencia para poder removerlo más tarde
        eventListenerRegistration = eventosRef.whereEqualTo("idActividad", idActividad)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Error escuchando los eventos", e);
                        Toast.makeText(getActivity(), "No se pudo obtener eventos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<EventoClass> elements = new ArrayList<>(); // Inicializa tu lista de elementos aquí
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        EventoClass evento = document.toObject(EventoClass.class);
                        evento.setEventId(document.getId());
                        elements.add(evento); // Añade eventos a la lista
                    }

                    // Inicializa y establece el adaptador aquí para asegurar que los datos están disponibles
                    if (adapter == null) {
                        adapter = new EventosAdapter(elements, getActivity(), this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.setListaEventos(elements); // Suponiendo que tienes un método para actualizar los datos
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onEliminarClicked(int position) {
        if (position != RecyclerView.NO_POSITION) {
            if (position < 0 || position >= elements.size()) {
                Log.e(TAG, "Posición inválida: " + position);
                return;
            }

            EventoClass eventoAEliminar = elements.get(position);
            String eventId = eventoAEliminar.getEventId();

            if (eventId == null) {
                Log.e(TAG, "Evento a eliminar con ID nulo");
                return;
            }

            Log.d(TAG, "Eliminando evento con ID: " + eventId);

            db.collection("Eventos").document(eventId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Evento eliminado: " + eventId);
                        elements.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, elements.size());
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error al eliminar evento", e);
                        Toast.makeText(getContext(), "Error al eliminar evento", Toast.LENGTH_SHORT).show();
                    });
        }

    }

    @Override
    public void onEditarClicked(String eventoId, String actividadId) {
        Intent intent = new Intent(getActivity(), NuevoEventoActivity.class);
        intent.putExtra("id_evento", eventoId);
        intent.putExtra("id_actividad", actividadId);
        startActivity(intent);
    }



}

