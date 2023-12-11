package com.example.weeking.entity;

public class EventoApoyo {
    private String eventoId;
    private String tipoApoyo;

    public EventoApoyo() {
    }

    public EventoApoyo(String eventoId, String tipoApoyo) {
        this.eventoId = eventoId;
        this.tipoApoyo = tipoApoyo;
    }

    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }

    public String getTipoApoyo() {
        return tipoApoyo;
    }

    public void setTipoApoyo(String tipoApoyo) {
        this.tipoApoyo = tipoApoyo;
    }
}

