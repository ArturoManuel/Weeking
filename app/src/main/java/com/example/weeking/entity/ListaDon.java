package com.example.weeking.entity;

public class ListaDon {
    private String nombre;
    private Integer monto;
    private boolean egresado;
    private String rechazo;
    private String foto;

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public ListaDon(String nombre, Integer monto, boolean egresado, String rechazo, String codigo) {
        this.nombre = nombre;
        this.monto = monto;
        this.egresado = egresado;
        this.rechazo = rechazo;
        this.codigo = codigo;
    }

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    public boolean isEgresado() {
        return egresado;
    }

    public void setEgresado(boolean egresado) {
        this.egresado = egresado;
    }

    public String getRechazo() {
        return rechazo;
    }

    public void setRechazo(String rechazo) {
        this.rechazo = rechazo;
    }

    private String codigo;

    public ListaDon() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
