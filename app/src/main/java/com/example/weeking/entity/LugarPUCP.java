package com.example.weeking.entity;

import com.google.android.gms.maps.model.LatLng;

public class LugarPUCP {
    private String nombre;
    private LatLng coordenadas;

    public LugarPUCP(String nombre, LatLng coordenadas) {
        this.nombre = nombre;
        this.coordenadas = coordenadas;
    }

    public String getNombre() {
        return nombre;
    }

    public LatLng getCoordenadas() {
        return coordenadas;
    }
}
