package com.example.weeking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
    }

    @Override
    public int getItemCount() {
        return (listaActividades != null) ? listaActividades.size() : 0;
    }

    public class ActividadViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView descripcionTextView;
        // Agrega otros componentes que necesites

        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreActividad);
            // Inicializa otros componentes

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onActividadClick(listaActividades.get(position));
                }
            });

        }

        void bindData(Actividad actividad) {
            nombreTextView.setText(actividad.getNombre());
            // Establece datos para otros componentes, si es necesario
        }
    }
    public interface OnActividadClickListener {
        void onActividadClick(Actividad actividad);
    }

    private OnActividadClickListener listener;

    public void setOnActividadClickListener(OnActividadClickListener listener) {
        this.listener = listener;
    }



}


