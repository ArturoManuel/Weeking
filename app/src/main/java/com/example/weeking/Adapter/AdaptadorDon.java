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
import com.example.weeking.entity.ListaDon;
import com.example.weeking.workers.ActividadesActivity;
import com.example.weeking.workers.NuevoEventoActivity;

import java.util.List;

public class AdaptadorDon extends RecyclerView.Adapter<AdaptadorDon.ViewHolder>{
    private List<ListaDon> mdata;
    private LayoutInflater minflater;
    private Context context;

    public AdaptadorDon(List<ListaDon> itemlist, Context context){
        this.minflater = LayoutInflater.from(context);
        this.context =context;
        this.mdata = itemlist;
    }

    @Override
    public AdaptadorDon.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = minflater.inflate(R.layout.lista_don, null);
        return new AdaptadorDon.ViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return mdata.size();
    }
    @Override
    public void onBindViewHolder(final AdaptadorDon.ViewHolder holder,final int position){
        holder.bindData(mdata.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre,monto;

        Button veri;
        ViewHolder(View itemView){
            super(itemView);
            nombre = itemView.findViewById(R.id.txtnombredon);
            monto = itemView.findViewById(R.id.txtmonto);
            veri = itemView.findViewById(R.id.btnVeri);
        }

        void bindData(final ListaDon item){
            nombre.setText(item.getNombre()+" - "+item.getCodigo());
            monto.setText("S/"+item.getMonto());
            if(item.getVeri()){
                veri.setText("Verificado");
                veri.setEnabled(false);
            }else {
                veri.setEnabled(true);
                veri.setText("Verificar");
            }
        }
    }

}
