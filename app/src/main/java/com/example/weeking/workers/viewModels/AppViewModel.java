package com.example.weeking.workers.viewModels;

import static android.content.ContentValues.TAG;

import static java.time.temporal.ChronoUnit.DAYS;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weeking.entity.Actividad;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.ListaDon;
import com.example.weeking.entity.Usuario;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AppViewModel extends ViewModel {

    private ListenerRegistration eventosListener;
    private final MutableLiveData<List<EventoClass>> listaEventos= new MutableLiveData<>();
    private final MutableLiveData<List<ListaDon>> listaDona= new MutableLiveData<>();
    public final MutableLiveData<List<Actividad>> listaActividades = new MutableLiveData<>(); // Añadido
    public final MutableLiveData<Usuario> currentUser = new MutableLiveData<>();
    public MutableLiveData<List<Actividad>> getListaActividades() {
        return listaActividades;
    }
    public MutableLiveData<List<EventoClass>> getListaEventos() {
        return listaEventos;
    }
    public MutableLiveData<List<ListaDon>> getListaDona() {
        return listaDona;
    }
    public MutableLiveData<List<EventoClass>> getEventosByLikes() {
        MutableLiveData<List<EventoClass>> eventosSortedByLikes = new MutableLiveData<>();

        getListaEventos().observeForever(eventoClasses -> {
            if (eventoClasses != null) {
                List<EventoClass> sortedList = new ArrayList<>(eventoClasses);
                Collections.sort(sortedList, (evento1, evento2) -> Integer.compare(evento2.getLikes(), evento1.getLikes()));
                // Limitar a los 5 eventos con más "likes"
                List<EventoClass> top5List = sortedList.size() > 5 ? sortedList.subList(0, 5) : sortedList;
                eventosSortedByLikes.setValue(top5List);
            }
        });
        return eventosSortedByLikes;
    }
    public void cargarDatosDeFirestore(FirebaseFirestore db) {
        db.collection("activity").addSnapshotListener((collection, error) -> {
            if (error != null) {
                Log.w(MotionEffect.TAG, "Error listening for document changes.", error);
                return;
            }
            if (collection != null && !collection.isEmpty()) {
                List<Actividad> elements = new ArrayList<>();
                for (QueryDocumentSnapshot document : collection) {
                    Actividad actividad = document.toObject(Actividad.class);
                    elements.add(actividad);
                }
                listaActividades.setValue(elements);  // Actualizado para listaActividades
            }
        });
    }


    public MutableLiveData<List<EventoClass>> getEventosByActividadId(String actividadId) {
        MutableLiveData<List<EventoClass>> eventosByActividadId = new MutableLiveData<>();

        getListaEventos().observeForever(eventoClasses -> {
            if (eventoClasses != null) {
                List<EventoClass> filteredList = eventoClasses.stream()
                        .filter(evento -> actividadId.equals(evento.getIdActividad()))
                        .collect(Collectors.toList());
                eventosByActividadId.setValue(filteredList);
            }
        });

        return eventosByActividadId;
    }





    public void iniciarListenerEventos() {
        if (eventosListener == null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            eventosListener = db.collection("Eventos").addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    Log.e(TAG, "Error al escuchar cambios en los eventos", e);
                    return;
                }

                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    List<EventoClass> eventosActualizados = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        EventoClass evento = document.toObject(EventoClass.class);
                        evento.setEventId(document.getId());
                        Timestamp timestamp = document.getTimestamp("fecha_evento");
                        Boolean estado = document.getBoolean("estado");
                        Date dateS = new Date(timestamp.toDate().getTime());
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String dateOp = format.format(dateS);
                        LocalDate localDate = LocalDate.now();
                        long numberOFDays = DAYS.between(LocalDate.parse(dateOp), localDate);
                        if (numberOFDays <= 0){
                            eventosActualizados.add(evento);
                        }else{
                            evento.setEstado(false);
                            eventosActualizados.remove(evento);
                        }
                        Log.d("msg", String.valueOf(estado));
                    }
                    listaEventos.setValue(eventosActualizados);
                }
            });
        }
    }

    public void detenerListenerEventos() {
        if (eventosListener != null) {
            eventosListener.remove();
            eventosListener = null;
        }
    }

    public Task<List<String>> eliminarActividad(Actividad actividad) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String actividadId = actividad.getId();
        List<String> userIds = new ArrayList<>();

        // Eliminar registros en UsuarioActividad
        Task<Void> eliminarUsuariosActividadTask = db.collection("UsuarioActividad")
                .whereEqualTo("idActividad", actividadId)
                .get()
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    WriteBatch batch = db.batch();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userId = document.getString("usuarioId");
                        // Aquí eliminas el registro de UsuarioActividad
                        batch.delete(db.collection("UsuarioActividad").document(document.getId()));
                        if (userId != null) {
                            userIds.add(userId);
                            batch.update(db.collection("usuarios").document(userId), "activity", FieldValue.arrayRemove(actividadId));
                        }
                    }
                    return batch.commit();
                });

        // Eliminar eventos relacionados con la actividad
        Task<Void> eliminarEventosTask = db.collection("Eventos")
                .whereEqualTo("idActividad", actividadId)
                .get()
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    WriteBatch batch = db.batch();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        batch.delete(db.collection("Eventos").document(document.getId()));
                    }
                    return batch.commit();
                }).addOnSuccessListener(aVoid -> {
                    // Aquí es donde actualizas tu lista local después de eliminar con éxito los eventos asociados
                    removeEventosByActividadId(actividadId);
                });

        // Una vez que las tareas anteriores se hayan completado, procedemos a eliminar la actividad y retornar la lista de userIds
        return Tasks.whenAllSuccess(eliminarUsuariosActividadTask, eliminarEventosTask)
                .continueWithTask(task -> {
                    return db.collection("activity").document(actividadId).delete();
                }).continueWith(task -> {
                    if (task.isSuccessful()) {
                        List<Actividad> currentActividades = listaActividades.getValue();
                        if (currentActividades != null) {
                            currentActividades.remove(actividad);
                            listaActividades.setValue(currentActividades);
                            actualizarListaEventosDespuesDeEliminarActividad(actividadId);

                        }
                        Log.d("DEBUG", "Actividad y registros relacionados eliminados con éxito.");
                        return userIds;
                    } else {
                        throw task.getException();
                    }
                });
    }


    private void actualizarListaEventosDespuesDeEliminarActividad(String actividadId) {
        List<EventoClass> eventosActuales = listaEventos.getValue();
        if (eventosActuales != null) {
            List<EventoClass> eventosActualizados = eventosActuales.stream()
                    .filter(evento -> !evento.getIdActividad().equals(actividadId))
                    .collect(Collectors.toList());
            listaEventos.postValue(eventosActualizados);
        }
    }

    public void actualizarEventosPorActividad(String actividadId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Eventos").whereEqualTo("idActividad", actividadId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(TAG, "Error al escuchar cambios: ", error);
                            return;
                        }
                        List<EventoClass> nuevosEventos = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            EventoClass evento = document.toObject(EventoClass.class);
                            evento.setEventId(document.getId());
                            nuevosEventos.add(evento);
                        }
                        MutableLiveData<List<EventoClass>> liveData = getEventosByActividadId(actividadId);
                        liveData.setValue(nuevosEventos);
                    }
                });
    }


    public void agregarOActualizarEvento(EventoClass evento) {
        List<EventoClass> eventosActuales = listaEventos.getValue();
        if (eventosActuales == null) {
            eventosActuales = new ArrayList<>();
        }
        eventosActuales.removeIf(e -> e.getEventId().equals(evento.getEventId())); // Elimina el evento anterior si existe
        eventosActuales.add(evento); // Agrega el evento actualizado o nuevo
        listaEventos.postValue(eventosActuales); // Actualiza el LiveData
    }





    public void removeEventosByActividadId(String actividadId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // Obtén la instancia de Firestore

        db.collection("Eventos")
                .whereEqualTo("idActividad", actividadId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Crear un batch para realizar todas las eliminaciones en una sola operación atómica
                        WriteBatch batch = db.batch();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Añadir cada documento para ser eliminado al batch
                            batch.delete(document.getReference());
                        }

                        // Ejecutar el batch
                        batch.commit().addOnCompleteListener(batchTask -> {
                            if (batchTask.isSuccessful()) {
                                // Si la eliminación en batch fue exitosa, actualizar la lista local
                                List<EventoClass> currentEventos = listaEventos.getValue();
                                if (currentEventos != null) {
                                    currentEventos.removeIf(evento -> actividadId.equals(evento.getIdActividad()));
                                    listaEventos.setValue(currentEventos);
                                }
                            } else {
                                // Manejar errores...
                                Log.e(TAG, "Error eliminando eventos de Firestore", batchTask.getException());
                            }
                        });
                    } else {
                        // Manejar errores...
                        Log.e(TAG, "Error obteniendo eventos de Firestore", task.getException());
                    }
                });
    }

    public void eliminarEventoDeActividad(String idActividad, String idEvento) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference actividadRef = db.collection("activity").document(idActividad);

        actividadRef.update("listaEventosIds", FieldValue.arrayRemove(idEvento))
                .addOnSuccessListener(aVoid -> {
                    // Actualiza la lista de eventos en el ViewModel
                    List<EventoClass> currentEventos = listaEventos.getValue();
                    if (currentEventos != null) {
                        currentEventos.removeIf(evento -> evento.getEventId().equals(idEvento));
                        listaEventos.setValue(currentEventos); // Notifica a los observadores del cambio
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error eliminando evento de la actividad", e));
    }


    public void actualizarEventosPorActividadId(String actividadId) {
        // Suponiendo que ya tienes una lista de todos los eventos
        List<EventoClass> todosLosEventos = listaEventos.getValue();
        if (todosLosEventos != null) {
            List<EventoClass> eventosFiltrados = todosLosEventos.stream()
                    .filter(evento -> actividadId.equals(evento.getIdActividad()))
                    .collect(Collectors.toList());
            // Supongamos que tienes un LiveData separado para eventos por actividad
            MutableLiveData<List<EventoClass>> eventosPorActividadId = getEventosByActividadId(actividadId);
            eventosPorActividadId.setValue(eventosFiltrados);
        }
    }





    public void setCurrentUser(Usuario user) {
        currentUser.setValue(user);
    }

    public MutableLiveData<Usuario> getCurrentUser() {
        return currentUser;
    }
}
