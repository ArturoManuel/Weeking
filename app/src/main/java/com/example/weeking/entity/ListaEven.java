package com.example.weeking.entity;

public class ListaEven {
    private String nombre;
    private String foto;
    private String estado;

    public String getNombre() {
        return nombre;
    }

    public ListaEven(String nombre, String foto, String estado) {
        this.nombre = nombre;
        this.foto = foto;
        this.estado = estado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
