package com.example.weeking.workers.fragmentos;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weeking.Adapter.EventosAdapter;
import com.example.weeking.R;
import com.example.weeking.entity.Actividad;
import com.example.weeking.entity.EventoClass;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class EditarDelegadoActividad extends Fragment implements EventosAdapter.OnEventoListener{
    private Actividad actividad;
    private RecyclerView recyclerViewEventos;
    private EventosAdapter eventosAdapter;
    private List<EventoClass> listaEventos;
    private FirebaseFirestore db;

    public EditarDelegadoActividad() {
        // Required empty public constructor
        db = FirebaseFirestore.getInstance(); // Inicializa tu instancia de Firestore aquí
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_delegado_actividad, container, false);

        recyclerViewEventos = view.findViewById(R.id.recyclerDeEventos);
        recyclerViewEventos.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            actividad = (Actividad) getArguments().getSerializable("actividad");
            obtenerEventosDeFirestore(actividad.getId());
        }

        // Inicializa la lista y el adaptador. Aún no tienen datos.
        listaEventos = new ArrayList<>();
        eventosAdapter = new EventosAdapter(listaEventos, getContext(), this);
        recyclerViewEventos.setAdapter(eventosAdapter);

        return view;
    }

    private void obtenerEventosDeFirestore(String idActividad) {
        CollectionReference eventosRef = db.collection("Eventos");

        eventosRef.whereEqualTo("idActividad", idActividad)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Limpiar la lista antigua
                    listaEventos.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        EventoClass evento = document.toObject(EventoClass.class);
                        evento.setEventId(document.getId());
                        listaEventos.add(evento); // Añade eventos a la lista
                    }
                    // Notifica al adaptador que los datos han cambiado
                    eventosAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.w("Error", "Error obteniendo eventos", e);
                    Toast.makeText(getActivity(), "No se pudo obtener eventos", Toast.LENGTH_SHORT).show();
                });
    }

    public void onEliminarClicked(int position) {
        if (position != RecyclerView.NO_POSITION) {
            if (position < 0 || position >= listaEventos.size()) {
                Log.e(TAG, "Posición inválida: " + position);
                return;
            }

            EventoClass eventoAEliminar = listaEventos.get(position);
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
                        listaEventos.remove(position);
                        eventosAdapter.notifyItemRemoved(position);
                        eventosAdapter.notifyItemRangeChanged(position, listaEventos.size());
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error al eliminar evento", e);
                        Toast.makeText(getContext(), "Error al eliminar evento", Toast.LENGTH_SHORT).show();
                    });
        }

    }
}