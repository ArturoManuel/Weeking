package com.example.weeking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weeking.R;
import com.example.weeking.entity.EventoClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.EventoViewHolder> {

    private List<EventoClass> listaEventos;
    private Context context;

    public EventosAdapter(List<EventoClass> listaEventos, Context context) {
        this.listaEventos = listaEventos;
        this.context = context;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.evento_componente, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        EventoClass evento = listaEventos.get(position);
        holder.bindData(evento);
    }

    @Override
    public int getItemCount() {
        return (listaEventos != null) ? listaEventos.size() : 0; // Protección contra null
    }

    public class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        ImageView eventoImageView;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.textNombre);
            eventoImageView = itemView.findViewById(R.id.foto);
        }

        void bindData(EventoClass evento) {
            nombreTextView.setText(evento.getNombre());

            // Si utilizas Glide para cargar imágenes desde una URL
            String imageUrl = evento.getFoto();
            Glide.with(eventoImageView.getContext())
                    .load(imageUrl)
                    .into(eventoImageView);
        }
    }

}


