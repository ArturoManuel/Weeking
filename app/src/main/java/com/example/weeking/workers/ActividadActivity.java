package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.databinding.ActivityActividadBinding;
import com.example.weeking.entity.Actividad;
import com.example.weeking.workers.fragmentos.InfoFragmento;
import com.example.weeking.workers.fragmentos.ListaFragmento;

public class ActividadActivity extends AppCompatActivity {
    ActivityActividadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActividadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Actividad actividadSeleccionada = DataHolder.getInstance().getActividadSeleccionada();

        cargarInfoFragmento();
        cargarEventoFragmento();

        binding.addEvent.setOnClickListener(v -> {
            navigateToActivity(NuevoEventoActivity.class);
        });
    }

    private void cargarInfoFragmento() {
        InfoFragmento infoFragmento = new InfoFragmento();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.infoFragmento, infoFragmento);
        fragmentTransaction.commit();
    }

    private void cargarEventoFragmento() {
        ListaFragmento eventoFragment = new ListaFragmento();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.evento, eventoFragment);
        fragmentTransaction.commit();
    }

    private void navigateToActivity(Class<?> destinationClass) {
        Intent intent = new Intent(ActividadActivity.this, destinationClass);
        startActivity(intent);
    }

}