package com.example.weeking.entity;

public class EventoClass {

    String nombre;
    String descripción;
    String apoyos;
    String fecha_hora;

    int estado;


    public EventoClass(String nombre, String descripción, String apoyos, String fecha_hora, int estado) {
        this.nombre = nombre;
        this.descripción = descripción;
        this.apoyos = apoyos;
        this.fecha_hora = fecha_hora;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripción() {
        return descripción;
    }

    public void setDescripción(String descripción) {
        this.descripción = descripción;
    }

    public String getApoyos() {
        return apoyos;
    }

    public void setApoyos(String apoyos) {
        this.apoyos = apoyos;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
