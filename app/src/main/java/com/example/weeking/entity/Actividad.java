package com.example.weeking.entity;

import java.util.List;

public class Actividad {
    private String id;
    private String nombre;
    private List<String> listaEventosIds;  // Lista de IDs de eventos

    // Constructor, getters y setters...


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getListaEventosIds() {
        return listaEventosIds;
    }

    public void setListaEventosIds(List<String> listaEventosIds) {
        this.listaEventosIds = listaEventosIds;
    }
}

