package com.example.weeking.workers.viewModels;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weeking.entity.Actividad;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.ListaDon;
import com.example.weeking.entity.Usuario;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppViewModel extends ViewModel {
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

    public void eliminarActividad(Actividad actividad) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String actividadId = actividad.getId();

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
                });

        // Una vez que las tareas anteriores se hayan completado, procedemos a eliminar la actividad
        Tasks.whenAllSuccess(eliminarUsuariosActividadTask, eliminarEventosTask).addOnSuccessListener(tasks -> {
                    db.collection("activity").document(actividadId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                List<Actividad> currentActividades = listaActividades.getValue();
                                if (currentActividades != null) {
                                    currentActividades.remove(actividad);
                                    listaActividades.setValue(currentActividades);
                                }
                                Log.d("DEBUG", "Actividad y registros relacionados eliminados con éxito.");
                            })
                            .addOnFailureListener(e -> {
                                Log.w("ERROR", "Error eliminando la actividad", e);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.w("ERROR", "Error eliminando registros relacionados con la actividad", e);
                });
    }





    public void setCurrentUser(Usuario user) {
        currentUser.setValue(user);
    }

    public MutableLiveData<Usuario> getCurrentUser() {
        return currentUser;
    }
}
