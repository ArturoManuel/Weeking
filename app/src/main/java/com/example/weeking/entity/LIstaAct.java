package com.example.weeking.entity;

public class LIstaAct {
    public LIstaAct(String actividad, String foto) {
        this.actividad = actividad;
        this.foto = foto;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String actividad;
    public String foto;
}
