package com.example.weeking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weeking.R;
import com.example.weeking.entity.Actividad;

import java.util.List;

public class DelegadoActividadesAdapter extends RecyclerView.Adapter<DelegadoActividadesAdapter.ActividadViewHolder> {

    private List<Actividad> listaActividades;
    private LayoutInflater inflater;
    private Context context;

    public DelegadoActividadesAdapter(List<Actividad> listaActividades, Context context) {
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
        // Agrega otros componentes que necesites
        ImageButton ejemploButton;

        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreActividad);
            ejemploButton = itemView.findViewById(R.id.btnEliminarActividad);
            // Inicializa otros componentes si es necesario

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
            ejemploButton.setVisibility(View.GONE);
        }
    }

    public interface OnActividadClickListener {
        void onActividadClick(Actividad actividad);
    }

    private OnActividadClickListener listener;

    public void setOnActividadClickListener(OnActividadClickListener listener) {
        this.listener = listener;
    }

    public void setActividades(List<Actividad> actividades) {
        this.listaActividades = actividades;
        notifyDataSetChanged(); // Notifica al adaptador para refrescar la vista
    }

}
