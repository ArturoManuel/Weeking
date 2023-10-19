package com.example.weeking.entity;

import java.io.Serializable;
import java.util.List;

public class EventoDto implements Serializable {


    List<EventoClass> lista;

    public List<EventoClass> getLista() {
        return lista;
    }

    public void setLista(List<EventoClass> lista) {
        this.lista = lista;
    }
}
