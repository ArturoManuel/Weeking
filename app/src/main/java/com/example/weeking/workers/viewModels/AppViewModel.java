package com.example.weeking.workers.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weeking.entity.EventoClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppViewModel extends ViewModel {

    private final MutableLiveData<List<EventoClass>> listaEventos= new MutableLiveData<>();

    public MutableLiveData<List<EventoClass>> getListaEventos() {
        return listaEventos;
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

}
