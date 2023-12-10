package com.example.weeking.entity;

import java.util.List;

public class Alumno {


    String nombre;

    private String imagen_url;
    private String authUID;
    private String estado;
    private String codigo;

    private String rol;

    private List<String> listaIdEvent;

    private List<String> activity;





    public Alumno() {
    }

    public Alumno(String nombre ,String rol , String codigo, List<String> activity) {
        this.nombre = nombre;
        this.rol=rol;
        this.codigo=codigo;
        this.activity=activity;
    }

    public Alumno(String nombre, String imagen_url, String authUID, String estado, String codigo) {
        this.nombre = nombre;
        this.imagen_url = imagen_url;
        this.authUID = authUID;
        this.estado = estado;
        this.codigo = codigo;
    }


    public List<String> getActivity() {
        return activity;
    }

    public void setActivity(List<String> activity) {
        this.activity = activity;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getImagen_url() {
        return imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }

    public String getAuthUID() {
        return authUID;
    }


    public void setAuthUID(String authUID) {
        this.authUID = authUID;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<String> getListaIdEvent() {
        return listaIdEvent;
    }

    public void setListaIdEvent(List<String> listaIdEvent) {
        this.listaIdEvent = listaIdEvent;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
