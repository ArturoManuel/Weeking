package com.example.weeking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import com.example.weeking.R;
import com.example.weeking.entity.EventoClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdaptadorPrin extends RecyclerView.Adapter<AdaptadorPrin.ViewHolder> {
    private List<EventoClass> mdata;
    private LayoutInflater minflater;
    private Context context;

    public AdaptadorPrin(List<EventoClass> itemlist, Context context) {
        this.minflater = LayoutInflater.from(context);
        this.context = context;
        this.mdata = itemlist;
    }

    public void updateData(List<EventoClass> newData) {
        if (this.mdata != null) {
            this.mdata.clear();
            this.mdata.addAll(newData);
        } else {
            this.mdata = newData;
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = minflater.inflate(R.layout.lista_principal, parent, false); // Añadido parent y false
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return (mdata != null) ? mdata.size() : 0;  // Protección contra null
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(mdata.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView evento;
        ImageView foto;
        TextView fecha;
        TextView ubicacion;

        ViewHolder(View itemView) {
            super(itemView);
            evento = itemView.findViewById(R.id.nombreEvento);
            foto = itemView.findViewById(R.id.foto);
            fecha=itemView.findViewById(R.id.fecha);
            ubicacion=itemView.findViewById(R.id.ubicacion);
        }

        void bindData(final EventoClass item) {
            evento.setText(item.getNombre());

            Date date = item.getFecha_evento().toDate();
            // Formatear la fecha
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d HH:mm", Locale.getDefault());
            String formattedDate = sdf.format(date);
            // Capitalizar la primera letra de cada palabra
            formattedDate = capitalize(formattedDate);

            fecha.setText(formattedDate);
            ubicacion.setText(item.getUbicacion());

            String imageUrl = item.getFoto();  // Asume que `item.getImageUrl()` proporciona la URL de la imagen
            Glide.with(foto.getContext())
                    .load(imageUrl)
                    .into(foto);  // `foto` es tu ImageView
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

}
