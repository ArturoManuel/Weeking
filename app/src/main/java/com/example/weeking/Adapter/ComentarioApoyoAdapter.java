package com.example.weeking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weeking.R;
import com.example.weeking.entity.ComentarioDeApoyo;
import com.example.weeking.entity.Usuario;

import java.util.List;

public class ComentarioApoyoAdapter extends RecyclerView.Adapter<ComentarioApoyoAdapter.ComentarioApoyoViewHolder> {

    private List<Usuario> comentariosApoyoList;
    private LayoutInflater mInflater;
    private Context context;

    public ComentarioApoyoAdapter(Context context, List<Usuario> comentariosApoyoList) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.comentariosApoyoList = comentariosApoyoList;
    }

    @NonNull
    @Override
    public ComentarioApoyoAdapter.ComentarioApoyoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_comentario_apoyo, parent, false);
        return new ComentarioApoyoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioApoyoAdapter.ComentarioApoyoViewHolder holder, int position) {
        Usuario usuario = comentariosApoyoList.get(position);
        holder.bindData(usuario);
    }

    @Override
    public int getItemCount() {
        return comentariosApoyoList != null ? comentariosApoyoList.size() : 0;
    }

    public class ComentarioApoyoViewHolder extends RecyclerView.ViewHolder {
        // Aquí debes definir las vistas que tienes en tu item_comentario_apoyo.xml
        // Por ejemplo:
        TextView tvNombre;
        TextView tvCodigo;
        TextView tvTipoApoyo;

        public ComentarioApoyoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvTipoApoyo = itemView.findViewById(R.id.tvTipoApoyo);
        }

        void bindData(Usuario usuario) {
            // Aquí asignas los valores de los atributos de Usuario a las vistas
            tvNombre.setText("Nombre:"+usuario.getNombre());
            tvCodigo.setText("Codigo:"+usuario.getCodigo());
            tvTipoApoyo.setText("Apoyo:"+usuario.getApoyo());
        }
    }
}
