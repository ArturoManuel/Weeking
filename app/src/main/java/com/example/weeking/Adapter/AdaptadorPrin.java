package com.example.weeking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weeking.R;
import com.example.weeking.entity.ListaDon;
import com.example.weeking.entity.ListaEven;

import java.util.List;

public class AdaptadorPrin extends RecyclerView.Adapter<AdaptadorPrin.ViewHolder>{
    private List<ListaEven> mdata;
    private LayoutInflater minflater;
    private Context context;

    public AdaptadorPrin(List<ListaEven> itemlist, Context context){
        this.minflater = LayoutInflater.from(context);
        this.context =context;
        this.mdata = itemlist;
    }

    @Override
    public AdaptadorPrin.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = minflater.inflate(R.layout.lista_principal, null);
        return new AdaptadorPrin.ViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return mdata.size();
    }
    @Override
    public void onBindViewHolder(final AdaptadorPrin.ViewHolder holder,final int position){
        holder.bindData(mdata.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imagen;
        TextView evento, estado;
        ViewHolder(View itemView){
            super(itemView);
            evento = itemView.findViewById(R.id.txtEvento1);
        }
        void bindData(final ListaEven item){
            evento.setText(item.getNombre());
        }
    }

}