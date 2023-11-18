package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.databinding.ActivityVistaEventoBinding;
import com.example.weeking.entity.EventoClass;

public class VistaEventoActivity extends AppCompatActivity {
    ActivityVistaEventoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button btnVerFotos = findViewById(R.id.btnDonaciones);
        binding.btnDonaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VistaEventoActivity.this, GaleriaEventos.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        TextView descrip = findViewById(R.id.textView15);
        TextView evento = findViewById(R.id.textView5);
        TextView ubi = findViewById(R.id.ubicacion1);
        ImageView imagen = findViewById(R.id.imageView2);

        EventoClass eventoSeleccionado = DataHolder.getInstance().getEventoSeleccionado();
        String even = (eventoSeleccionado.getNombre() != null) ? eventoSeleccionado.getNombre() : "Falta llenar el campo nombre del evento";
        String descri = (eventoSeleccionado.getDescripcion() != null) ? eventoSeleccionado.getDescripcion() : "Falta llenar el campo descripción";
        String ubica = (eventoSeleccionado.getUbicacion() != null) ? eventoSeleccionado.getUbicacion() : "Falta llenar el campo ubicación";
        String foto = eventoSeleccionado.getFoto();
        evento.setText(even);
        descrip.setText(descri);
        ubi.setText(ubica);
        if (foto != null && !foto.isEmpty()) {
            Glide.with(imagen.getContext())
                    .load(foto)
                    .into(imagen);
        }

    }
}