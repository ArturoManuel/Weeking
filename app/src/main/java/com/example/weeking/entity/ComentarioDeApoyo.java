package com.example.weeking.entity;

public class ComentarioDeApoyo {
    private String comentario;
    private String tipoApoyo;

    // Constructor vacío necesario para Firestore
    public ComentarioDeApoyo() {}

    // Constructor con todos los campos
    public ComentarioDeApoyo(String comentario, String tipoApoyo) {
        this.comentario = comentario;
        this.tipoApoyo = tipoApoyo;
    }

    // Getters y setters
    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getTipoApoyo() {
        return tipoApoyo;
    }

    public void setTipoApoyo(String tipoApoyo) {
        this.tipoApoyo = tipoApoyo;
    }
}
