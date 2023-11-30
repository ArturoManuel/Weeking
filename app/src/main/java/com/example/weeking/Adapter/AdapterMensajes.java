package com.example.weeking.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weeking.R;
import com.example.weeking.entity.Mensaje;
import com.google.firebase.Timestamp;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterMensajes extends RecyclerView.Adapter<AdapterMensajes.ViewHolder> {

    private List<Mensaje> mdata;
    private LayoutInflater minflater;
    private Context context;

    public AdapterMensajes(List<Mensaje> itemlist, Context context){
        this.minflater = LayoutInflater.from(context);
        this.context =context;
        this.mdata = itemlist;
    }

    @Override
    public AdapterMensajes.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = minflater.inflate(R.layout.card_view_mensajes, null);
        return new AdapterMensajes.ViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return mdata.size();
    }
    @Override
    public void onBindViewHolder(final AdapterMensajes.ViewHolder holder, final int position){
        holder.bindData(mdata.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre,hora,mensaje;
        ImageView imagen;
        CircleImageView perfil;

        ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreMensaje);
            perfil = itemView.findViewById(R.id.fotoPerfilMensaje);
            hora = itemView.findViewById(R.id.horaMensaje);
            imagen = itemView.findViewById(R.id.mensajeFoto);
            mensaje = itemView.findViewById(R.id.mensajeMensaje);
            }

        void bindData(final Mensaje item) {
            if (item != null) {
                nombre.setText(item.getNombre());
                Picasso.get().load(item.getFotoPerfil()).into(perfil);
                Instant instant = Instant.ofEpochMilli(item.getFecha());
                LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = dateTime.format(formatter);
                hora.setText(formattedDateTime);
                if(!item.getUrlFoto().equals("")){
                    Log.d("zxc",item.getType_mensaje());
                    Picasso.get().load(item.getUrlFoto()).into(imagen);
                    imagen.setVisibility(View.VISIBLE);
                    mensaje.setVisibility(View.GONE);

                }else {
                    mensaje.setText(item.getMensaje());
                    mensaje.setVisibility(View.VISIBLE);
                    imagen.setVisibility(View.GONE);
                }

        }
    }}

}