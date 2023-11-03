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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
        // Primero, eliminar los eventos asociados con la actividad
        db.collection("Eventos")
                .whereEqualTo("idActividad", actividad.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("Eventos").document(document.getId()).delete();
                        }

                        // Luego, eliminar la actividad de Firestore
                        db.collection("activity").document(actividad.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Eliminación exitosa, ahora actualiza la lista localmente
                                    List<Actividad> currentActividades = listaActividades.getValue();
                                    if (currentActividades != null) {
                                        currentActividades.remove(actividad);
                                        listaActividades.setValue(currentActividades);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.w(TAG, "Error eliminando la actividad", e);
                                });
                    } else {
                        Log.w(TAG, "Error obteniendo eventos asociados con la actividad", task.getException());
                    }
                });
    }



    public void setCurrentUser(Usuario user) {
        currentUser.setValue(user);
    }

    public MutableLiveData<Usuario> getCurrentUser() {
        return currentUser;
    }
}
