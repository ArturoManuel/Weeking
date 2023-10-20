package com.example.weeking.dataHolder;

import com.example.weeking.entity.EventoClass;

public class DataHolder {

        private static final DataHolder ourInstance = new DataHolder();
        private EventoClass eventoSeleccionado;

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



}
