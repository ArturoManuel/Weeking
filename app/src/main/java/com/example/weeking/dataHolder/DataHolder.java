package com.example.weeking.dataHolder;

import com.example.weeking.entity.Actividad;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.ListaDon;
import com.example.weeking.entity.Usuario;

public class DataHolder {

        private static DataHolder ourInstance;
         private Actividad actividadSeleccionada;

        private EventoClass eventoSeleccionado;

        private ListaDon donacionseleccionado;

    public Usuario getUsuarioseleccionado() {
        return usuarioseleccionado;
    }

    public void setUsuarioseleccionado(Usuario usuarioseleccionado) {
        this.usuarioseleccionado = usuarioseleccionado;
    }

    private Usuario usuarioseleccionado;

    public ListaDon getDonacionseleccionado() {
        return donacionseleccionado;
    }

    public void setDonacionseleccionado(ListaDon donacionseleccionado) {
        this.donacionseleccionado = donacionseleccionado;
    }

    public static DataHolder getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataHolder();
        }
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

    public void resetEventoSeleccionado() {
        eventoSeleccionado = null;
    }


}
