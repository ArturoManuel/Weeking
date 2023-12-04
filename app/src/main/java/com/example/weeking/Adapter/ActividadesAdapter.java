package com.example.weeking.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.weeking.R;
import com.example.weeking.entity.Actividad;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActividadesAdapter extends RecyclerView.Adapter<ActividadesAdapter.ActividadViewHolder> {

    private List<Actividad> listaActividades;
    private LayoutInflater inflater;
    private Context context;



    public ActividadesAdapter(List<Actividad> listaActividades, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listaActividades = listaActividades;
    }

    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.lista_act, parent, false);
        return new ActividadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {
        Actividad actividad = listaActividades.get(position);
        holder.bindData(actividad);
        // Establecer el listener para el botón de eliminar



        holder.eliminarImageView.setOnClickListener(v -> {
            // Crear un AlertDialog para confirmar la eliminación
            new AlertDialog.Builder(context)
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Estás seguro de que deseas eliminar esta actividad?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        // Usuario confirma la eliminación
                        if (listener != null) {
                            listener.onEliminarActividadClick(position);
                        }
                    })
                    .setNegativeButton("Cancelar", null) // null significa que no hace nada al clickear
                    .show();
        });


    }

    @Override
    public int getItemCount() {
        return (listaActividades != null) ? listaActividades.size() : 0;
    }

    public class ActividadViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView descripcionTextView;
        ImageView eliminarImageView;

        ImageButton imagenActividad;
        // Agrega otros componentes que necesites

        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreActividad);
            eliminarImageView = itemView.findViewById(R.id.btnEliminarActividad);
            imagenActividad = itemView.findViewById(R.id.imaBtnVerEventos);
            // Inicializa otros componentes


            imagenActividad.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onActividadClick(listaActividades.get(position));
                }
            });

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onActividadClick(listaActividades.get(position));
                }
            });

        }

        void bindData(Actividad actividad) {
            nombreTextView.setText(actividad.getNombre());
            Glide.with(itemView.getContext())
                    .load(actividad.getImagenUrl())
                    .into(imagenActividad);

        }
    }
    public interface OnActividadClickListener {
        void onActividadClick(Actividad actividad);
        void onEliminarActividadClick(int position);
    }

    private OnActividadClickListener listener;

    public void setOnActividadClickListener(OnActividadClickListener listener) {
        this.listener = listener;
    }



}


