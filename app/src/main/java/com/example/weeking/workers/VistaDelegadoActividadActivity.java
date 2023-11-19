package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.weeking.R;

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
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        });


    }
}