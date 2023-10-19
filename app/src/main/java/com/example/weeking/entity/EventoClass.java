package com.example.weeking.entity;

import com.google.firebase.Timestamp;


import com.google.firebase.firestore.GeoPoint;

public class EventoClass {

    private String descripcion; // "Se jugar un juego"
    private Boolean estado; // true
    private Timestamp fecha_evento; // "29 de octubre de 2023, 00:52:29 UTC-5"
    private String foto; // URL de la foto
    private Integer likes; // 4
    private String nombre; // "pepito"
    private String ubicacion; //

    // Constructor vacío necesario para Firestore
    public EventoClass() {
    }


    public EventoClass(String descripcion, Boolean estado, Timestamp fecha_evento, String foto, Integer likes, String nombre, String ubicacion) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.fecha_evento = fecha_evento;
        this.foto = foto;
        this.likes = likes;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Boolean isEstado() {
        return estado;
    }

    public Timestamp getFecha_evento() {
        return fecha_evento;
    }

    public String getFoto() {
        return foto;
    }

    public Integer getLikes() {
        return likes;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public void setFechaEvento(Timestamp fecha_evento) {
        this.fecha_evento = fecha_evento;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
