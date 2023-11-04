package com.example.weeking.entity;

import java.io.Serializable;
import java.util.List;

public class Actividad implements Serializable {
    private String id;
    private String nombre;
    private String descripcion;
    private int seguidores;
    private List<String> listaEventosIds;
    private String imagenUrl;



    public Actividad() {
    }

    public Actividad(String id, String nombre, String descripcion, int seguidores, List<String> listaEventosIds) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.seguidores = seguidores;
        this.listaEventosIds = listaEventosIds;
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public List<String> getListaEventosIds() {
        return listaEventosIds;
    }

    public void setListaEventosIds(List<String> listaEventosIds) {
        this.listaEventosIds = listaEventosIds;
    }
    public String getImagenUrl() {
        return imagenUrl;
    }

    // Método setter para la URL de la imagen
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}

