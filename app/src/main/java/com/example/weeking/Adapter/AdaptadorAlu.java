package com.example.weeking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.entity.LIstaAct;
import com.example.weeking.entity.ListaDon;
import com.example.weeking.entity.Usuario;
import com.example.weeking.workers.Verificacion_don;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorAlu extends RecyclerView.Adapter<AdaptadorAlu.ViewHolder>{
    private List<Usuario> mdata;
    private LayoutInflater minflater;
    private Context context;

    public AdaptadorAlu(List<Usuario> itemlist, Context context){
        this.minflater = LayoutInflater.from(context);
        this.context =context;
        this.mdata = itemlist;
    }

    @Override
    public AdaptadorAlu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = minflater.inflate(R.layout.lista_alu, null);
        return new AdaptadorAlu.ViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return mdata.size();
    }
    @Override
    public void onBindViewHolder(final AdaptadorAlu.ViewHolder holder,final int position){
        holder.bindData(mdata.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, codigo, estado;
        ImageView imagen;

        ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            codigo = itemView.findViewById(R.id.codigo);
            estado = itemView.findViewById(R.id.estado);
            imagen = itemView.findViewById(R.id.imageView12);
        }

        void bindData(final Usuario item) {
            if (item != null) {
                nombre.setText(item.getNombre());
                codigo.setText(item.getCodigo());
                estado.setText(item.getEstado());
                if (item.getImagen_url().equals("tu_url_por_defecto_aqui")) {
                    Picasso.get().load("https://cdn-icons-png.flaticon.com/512/3135/3135768.png").into(imagen);
                } else {
                    Picasso.get().load(item.getImagen_url()).into(imagen);
                }
            }
        }
    }
}
