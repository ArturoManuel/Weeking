package com.example.weeking.dataHolder;

import com.example.weeking.entity.Actividad;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.ListaDon;

public class DataHolder {

        private static final DataHolder ourInstance = new DataHolder();
         private Actividad actividadSeleccionada;

        private EventoClass eventoSeleccionado;

        private ListaDon donacionseleccionado;

    public ListaDon getDonacionseleccionado() {
        return donacionseleccionado;
    }

    public void setDonacionseleccionado(ListaDon donacionseleccionado) {
        this.donacionseleccionado = donacionseleccionado;
    }

    public static DataHolder getInstance() {
            return ourInstance;
        }

        private DataHolder() {
        }

        public void setEventoSeleccionado(EventoClass evento) {
            this.eventoSeleccionado = evento;
        }

        public EventoClass getEventoSeleccionado() {
            return eventoSeleccionado;
        }




    public Actividad getActividadSeleccionada() {
        return actividadSeleccionada;
    }

    public void setActividadSeleccionada(Actividad actividad) {
        this.actividadSeleccionada = actividad;
    }




}
