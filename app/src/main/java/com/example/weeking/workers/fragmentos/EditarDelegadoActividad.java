package com.example.weeking.workers.fragmentos;

import static android.content.ContentValues.TAG;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.Adapter.EventosAdapter;
import com.example.weeking.R;
import com.example.weeking.entity.Actividad;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.workers.ActividadActivity;
import com.example.weeking.workers.NuevoEventoActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class EditarDelegadoActividad extends Fragment implements EventosAdapter.OnEventoListener{
    private Actividad actividad;
    private RecyclerView recyclerViewEventos;
    private EventosAdapter eventosAdapter;
    private List<EventoClass> listaEventos;
    private FirebaseFirestore db;
    private ListenerRegistration eventosListenerRegistration;

    private  String idactividad;

    private TextView nombreActividadTextView;

    private TextView descripcionText;


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
        Button btnAnadir = view.findViewById(R.id.btn_add); // Asegúrate de que "btnAnadir" es el ID correcto de tu botón

        btnAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNuevoEventoActivityConIdActividad(actividad.getId().toString(),"no_evento");
            }
        });;
        // Inicializa la lista y el adaptador. Aún no tienen datos.


        listaEventos = new ArrayList<>();
        eventosAdapter = new EventosAdapter(listaEventos, getContext(), this);
        recyclerViewEventos.setAdapter(eventosAdapter);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nombreActividadTextView = view.findViewById(R.id.nombreActividad);
        descripcionText = view.findViewById(R.id.description);
        nombreActividadTextView.setText(actividad.getNombre());
        descripcionText.setText(actividad.getDescripcion());
    }



    private void obtenerEventosDeFirestore(String idActividad) {
        CollectionReference eventosRef = db.collection("Eventos");

        // Establece el listener y guarda la referencia para poder removerla más tarde
        eventosListenerRegistration = eventosRef.whereEqualTo("idActividad", idActividad)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // En caso de un error al establecer el listener
                            Log.w("Error", "Error al escuchar los eventos", e);
                            Toast.makeText(getActivity(), "No se pudo obtener eventos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Limpiar la lista antigua antes de agregar los nuevos eventos
                        listaEventos.clear();
                        for (QueryDocumentSnapshot document : value) {
                            EventoClass evento = document.toObject(EventoClass.class);
                            evento.setEventId(document.getId()); // Asegúrate de tener este método en tu clase EventoClass
                            listaEventos.add(evento); // Añade eventos a la lista
                        }
                        // Notifica al adaptador que los datos han cambiado
                        eventosAdapter.notifyDataSetChanged();
                    }
                });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (eventosListenerRegistration != null) {
            eventosListenerRegistration.remove(); // Remueve el listener
        }
    }


    @Override
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

    @Override
    public void onEditarClicked(String eventoId, String actividadId) {
        Intent intent = new Intent(getActivity(), NuevoEventoActivity.class);
        intent.putExtra("id_evento", eventoId);
        intent.putExtra("id_actividad", actividadId);
        startActivity(intent);
    }
    private void abrirNuevoEventoActivityConIdActividad(String actividadId,String eventoId) {
        Intent intent = new Intent(getActivity(), NuevoEventoActivity.class);
        intent.putExtra("id_evento", eventoId);
        intent.putExtra("id_actividad", actividadId); // asumiendo que 'idactividad' es el ID de la actividad actual.
        startActivity(intent);
    }

}