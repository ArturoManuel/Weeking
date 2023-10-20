package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.entity.EventoClass;

public class VistaEventoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_evento);
        Button btnVerFotos = findViewById(R.id.btnDonaciones);
        btnVerFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VistaEventoActivity.this, GaleriaEventos.class);
                startActivity(intent);
            }
        });

        EventoClass eventoSeleccionado = DataHolder.getInstance().getEventoSeleccionado();
        int posicion = getIntent().getIntExtra("posicion", -1);

        if (posicion == -1) {
            Log.d("msg-error","Algo salio mal");
        } else {
            // Usar el valor de 'posicion' para realizar alguna operación.
            Log.d("msg-error", String.valueOf(posicion)+eventoSeleccionado.getNombre().toString());
        }

    }
}