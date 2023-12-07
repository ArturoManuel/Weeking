package com.example.weeking.entity;

import com.google.firebase.Timestamp;


import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.List;

public class EventoClass {

    private String descripcion; // "Se jugar un juego"
    private Boolean estado; // true es en proceso, false es terminado
    private Timestamp fecha_evento; // "29 de octubre de 2023, 00:52:29 UTC-5"
    private String foto; // URL de la foto
    private Integer likes; // 4
    private String nombre; // "pepito"
    private String ubicacion; //

    private String idActividad;

    private double latitud;
    private double longitud;

    private  String eventId;

    private List<String> listaUsuariosIds;



    // Constructor vacío necesario para Firestore
    public EventoClass() {
    }


    public String getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(String idActividad) {
        this.idActividad = idActividad;
    }

    public List<String> getListaUsuariosIds() {
        return listaUsuariosIds;
    }


    public void setListaUsuariosIds(List<String> listaUsuariosIds) {
        this.listaUsuariosIds = listaUsuariosIds;
    }




    public EventoClass(String descripcion, Boolean estado, Timestamp fecha_evento, String foto, Integer likes, String nombre, String ubicacion, String eventId) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.fecha_evento = fecha_evento;
        this.foto = foto;
        this.likes = likes;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.eventId=eventId;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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
