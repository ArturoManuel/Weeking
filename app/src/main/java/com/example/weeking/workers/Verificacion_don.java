package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.ListaDon;

public class Verificacion_don extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion_don);
        ListaDon donacionSeleccionado = DataHolder.getInstance().getDonacionseleccionado();
        TextView nombre = findViewById(R.id.textView48);
        nombre.setText(donacionSeleccionado.getNombre()+" "+donacionSeleccionado.getCodigo());

        Button confirma = findViewById(R.id.button12);
        Button negar = findViewById(R.id.button13);
        confirma.setOnClickListener(v -> {
            Intent intent = new Intent(this, Don_ve.class);
            intent.putExtra("codigo",donacionSeleccionado.getCodigo());
            intent.putExtra("dona",donacionSeleccionado.getMonto());
            intent.putExtra("nombre",donacionSeleccionado.getNombre());
            intent.putExtra("recha",donacionSeleccionado.getRechazo());
            intent.putExtra("egre",donacionSeleccionado.isEgresado());
            startActivity(intent);
        });
        negar.setOnClickListener(v -> {
            Intent intent = new Intent(this, Don_recha.class);
            intent.putExtra("codigo",donacionSeleccionado.getCodigo());
            intent.putExtra("dona",donacionSeleccionado.getMonto());
            intent.putExtra("nombre",donacionSeleccionado.getNombre());
            intent.putExtra("recha",donacionSeleccionado.getRechazo());
            intent.putExtra("egre",donacionSeleccionado.isEgresado());
            startActivity(intent);
        });
    }
}