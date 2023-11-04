package com.example.weeking.entity;

import java.util.List;

public class Usuario {

    private List<String> activity;
    private String apoyo;
    private String authUID;
    private String codigo;
    private String correo;
    private String estado;
    private String imagen_url;
    private String nombre;
    private String rol;

    // Constructor vacío requerido para Firestore
    public Usuario() {}

    // Constructor completo
    public Usuario(List<String> activity, String apoyo, String authUID, String codigo, String correo, String estado, String imagen_url, String nombre, String rol) {
        this.activity = activity;
        this.apoyo = apoyo;
        this.authUID = authUID;
        this.codigo = codigo;
        this.correo = correo;
        this.estado = estado;
        this.imagen_url = imagen_url;
        this.nombre = nombre;
        this.rol = rol;
    }

    // Getters y Setters para cada campo

    public List<String> getActivity() {
        return activity;
    }

    public void setActivity(List<String> activity) {
        this.activity = activity;
    }

    public String getApoyo() {
        return apoyo;
    }

    public void setApoyo(String apoyo) {
        this.apoyo = apoyo;
    }

    public String getAuthUID() {
        return authUID;
    }

    public void setAuthUID(String authUID) {
        this.authUID = authUID;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getImagen_url() {
        return imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
