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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaFragmento extends Fragment implements EventosAdapter.OnEventoListener {
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

        eventosRef.whereEqualTo("idActividad", idActividad)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    elements = new ArrayList<>(); // Inicializa tu lista de elementos aquí
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        EventoClass evento = document.toObject(EventoClass.class);
                        evento.setEventId(document.getId());
                        elements.add(evento); // Asegúrate de que estás añadiendo a 'elements'
                    }
                    // Inicializa y establece el adaptador aquí para asegurar que los datos están disponibles
                    adapter = new EventosAdapter(elements, getActivity(), this); // Ahora pasas la lista correcta
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error obteniendo eventos", e);
                    Toast.makeText(getActivity(), "No se pudo obtener eventos", Toast.LENGTH_SHORT).show();
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




}

