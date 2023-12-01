package com.example.weeking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.ListaDon;
import com.example.weeking.workers.Chat;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorE extends RecyclerView.Adapter<AdaptadorE.ViewHolder>{
    private List<EventoClass> mdata;
    private LayoutInflater minflater;
    private Context context;

    public AdaptadorE(List<EventoClass> itemlist, Context context){
        this.minflater = LayoutInflater.from(context);
        this.context =context;
        this.mdata = itemlist;
    }

    @Override
    public AdaptadorE.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = minflater.inflate(R.layout.lista_even, null);
        return new AdaptadorE.ViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return mdata.size();
    }
    @Override
    public void onBindViewHolder(final AdaptadorE.ViewHolder holder,final int position){
        holder.bindData(mdata.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton imagen;
        TextView evento, estado;
        ViewHolder(View itemView){
            super(itemView);
            evento = itemView.findViewById(R.id.txtEvento);
            estado = itemView.findViewById(R.id.txtEstado);
            imagen = itemView.findViewById(R.id.imaBtnVerEventos);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                EventoClass even = mdata.get(position);
                DataHolder.getInstance().setEventoSeleccionado(even);
                Intent intent = new Intent (context, Chat.class);
                context.startActivity(intent);
            });
        }
        void bindData(final EventoClass item){
            evento.setText(item.getNombre());
            if(item.isEstado()){
                estado.setText("no finalizado");
            }else {
                estado.setText("finalizado");
            }
            Picasso.get().load(item.getFoto()).into(imagen);
        }
    }

}
