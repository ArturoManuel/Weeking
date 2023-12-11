package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.ListaDon;
import com.squareup.picasso.Picasso;

public class Verificacion_don extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion_don);
        ListaDon donacionSeleccionado = DataHolder.getInstance().getDonacionseleccionado();
        TextView nombre = findViewById(R.id.textView48);
        nombre.setText(donacionSeleccionado.getNombre()+" "+donacionSeleccionado.getCodigo());
        ImageView imagen = findViewById(R.id.imageView11);
        String imageUrl = donacionSeleccionado.getFoto(); // URL de la imagen que quieres cargar
        Picasso.get().load(imageUrl).into(imagen);
        Button confirma = findViewById(R.id.button12);
        Button negar = findViewById(R.id.button13);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Verficacion de Donacion");
        }
        confirma.setOnClickListener(v -> {
            Intent intent = new Intent(this, Don_ve.class);
            intent.putExtra("codigo",donacionSeleccionado.getCodigo());
            intent.putExtra("dona",donacionSeleccionado.getMonto());
            intent.putExtra("nombre",donacionSeleccionado.getNombre());
            intent.putExtra("recha",donacionSeleccionado.getRechazo());
            intent.putExtra("egre",donacionSeleccionado.isEgresado());
            startActivity(intent);
            finish();
        });
        negar.setOnClickListener(v -> {
            Intent intent = new Intent(this, Don_recha.class);
            intent.putExtra("codigo",donacionSeleccionado.getCodigo());
            intent.putExtra("dona",donacionSeleccionado.getMonto());
            intent.putExtra("nombre",donacionSeleccionado.getNombre());
            intent.putExtra("recha",donacionSeleccionado.getRechazo());
            intent.putExtra("egre",donacionSeleccionado.isEgresado());
            startActivity(intent);
            finish();
        });
    }
}