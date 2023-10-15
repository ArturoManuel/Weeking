package com.example.weeking.entity;

public class ListaDon {
    private String nombre;
    private int monto;
    private Boolean veri;

    public ListaDon(String nombre, int monto, Boolean veri, String codigo) {
        this.nombre = nombre;
        this.monto = monto;
        this.veri = veri;
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public Boolean getVeri() {
        return veri;
    }

    public void setVeri(Boolean veri) {
        this.veri = veri;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    private String codigo;

}
