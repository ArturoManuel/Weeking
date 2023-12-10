package com.example.weeking.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.workers.VistaEventoActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.EventoViewHolder> {

    private List<EventoClass> listaEventos;
    private Context context;
    private OnEventoListener onEventoListener;


    public EventosAdapter(List<EventoClass> listaEventos, Context context, OnEventoListener onEventoListener) {
        this.listaEventos = listaEventos;
        this.context = context;
        this.onEventoListener = onEventoListener;
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

        holder.eliminarImageView.setOnClickListener(v -> {
            int adapterPos = holder.getAdapterPosition();
            if(adapterPos != RecyclerView.NO_POSITION) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Estás seguro de que deseas eliminar el evento " + evento.getNombre() + "?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            // Usuario confirma la eliminación
                            onEventoListener.onEliminarClicked(adapterPos, evento.getEventId());
                        })
                        .setNegativeButton("Cancelar", null) // null significa que no hace nada al clickear
                        .show();
            }
        });


        holder.editarImageView.setOnClickListener(v -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION) {
                EventoClass eventoParaEditar = listaEventos.get(adapterPos);
                onEventoListener.onEditarClicked(eventoParaEditar.getEventId(), eventoParaEditar.getIdActividad());
            }
        });
    }


    @Override
    public int getItemCount() {
        return (listaEventos != null) ? listaEventos.size() : 0; // Protección contra null
    }

    public class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        ImageView eventoImageView;

        TextView fecha;

        ImageView eliminarImageView;

        ImageView editarImageView;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombre);
            eventoImageView = itemView.findViewById(R.id.foto);
            fecha = itemView.findViewById(R.id.fechaEvento);
            eliminarImageView=itemView.findViewById(R.id.btnEliminar);
            editarImageView=itemView.findViewById(R.id.btnEditar);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    EventoClass eventoSeleccionado = listaEventos.get(position);
                    DataHolder.getInstance().setEventoSeleccionado(eventoSeleccionado);
                    Intent intent = new Intent(context, VistaEventoActivity.class);
                    intent.putExtra("rol", "delegado");
                    context.startActivity(intent);
                }
            });
        }

        void bindData(EventoClass evento) {
            nombreTextView.setText(evento.getNombre().toString());

            Date date = evento.getFecha_evento().toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d HH:mm", Locale.getDefault());
            String formattedDate = sdf.format(date);
            // Capitalizar la primera letra de cada palabra
            formattedDate = capitalize(formattedDate);
            fecha.setText(formattedDate);
            // Si utilizas Glide para cargar imágenes desde una URL
            String imageUrl = evento.getFoto().toString();
            Glide.with(eventoImageView.getContext())
                    .load(imageUrl)
                    .into(eventoImageView);
        }
    }

    public String capitalize(String str) {
        // Verifica si la cadena está vacía o es nula.
        if(str == null || str.isEmpty()) {
            return str;
        }
        // Divide la cadena en palabras.
        String[] words = str.split(" ");
        StringBuilder sb = new StringBuilder();

        // Itera a través de cada palabra.
        for (String word : words) {
            // Convierte el primer carácter de cada palabra a mayúsculas.
            sb.append(Character.toUpperCase(word.charAt(0)));
            // Añade el resto de la palabra en minúsculas.
            sb.append(word.substring(1).toLowerCase());
            // Añade un espacio después de cada palabra.
            sb.append(" ");
        }

        // Elimina el espacio adicional al final y devuelve la cadena resultante.
        return sb.toString().trim();
    }
    public void setListaEventos(List<EventoClass> listaEventos) {
        this.listaEventos = listaEventos;
        notifyDataSetChanged();
        Log.d("Tamaño de la lista Adapter", String.valueOf(listaEventos.size()));
    }


    public interface OnEventoListener {
        void onEliminarClicked(int position ,String eventoId);
        void onEditarClicked(String eventoId, String actividadId);
    }







}


