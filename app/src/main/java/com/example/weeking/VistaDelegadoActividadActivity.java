package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VistaDelegadoActividadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_delegado_actividad);
        Button btnActividad =  findViewById(R.id.btnAssignActivity);
        btnActividad.setOnClickListener(v -> {
          Intent intent =  new Intent(VistaDelegadoActividadActivity.this, ActividadesActivity.class);
          startActivity(intent);
          finish();
        });

       /* Button btnActividad =  findViewById(R.id.btnAssignActivity);
        btnActividad.setOnClickListener(v -> {
            Intent intent =  new Intent(VistaDelegadoActividadActivity.this, ActividadesActivity.class);
            startActivity(intent);
            finish();
        });*/

        Button btnDonacion =  findViewById(R.id.btnDonaDA);
        btnDonacion.setOnClickListener(v -> {
            Intent intent =  new Intent(VistaDelegadoActividadActivity.this, Donacion.class);
            startActivity(intent);
            finish();
        });

        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
    }
}