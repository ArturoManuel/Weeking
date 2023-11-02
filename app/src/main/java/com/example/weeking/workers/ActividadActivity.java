package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.databinding.ActivityActividadBinding;
import com.example.weeking.entity.Actividad;
import com.example.weeking.workers.fragmentos.AnadirFragmento;
import com.example.weeking.workers.fragmentos.EditarFragmento;
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
        Log.d("msg", actividadSeleccionada.getId().toString());

        ListaFragmento fragmentoEventos = new ListaFragmento(actividadSeleccionada.getId().toString());

        binding.addEvent.setOnClickListener(v -> {
            String idActividad = actividadSeleccionada.getId().toString();
            if (idActividad != null && !idActividad.isEmpty()) {
                Intent intent = new Intent(ActividadActivity.this, NuevoEventoActivity.class);
                intent.putExtra("id_actividad", idActividad);
                startActivity(intent);
            } else {
                Log.d("msg", "ID de actividad es nulo o vacío");
            }
        });

        cargarInfoFragmento();
        cargarEventoFragmento(fragmentoEventos);
    }



    private void cargarInfoFragmento() {
        InfoFragmento infoFragmento = new InfoFragmento();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.infoFragmento, infoFragmento);
        fragmentTransaction.commit();
    }

    private void cargarEventoFragmento(ListaFragmento fragmentoEventos) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.evento, fragmentoEventos);
        fragmentTransaction.commit();
    }
    private void navigateToActivity(Class<?> destinationClass) {
        Intent intent = new Intent(ActividadActivity.this, destinationClass);
        startActivity(intent);
    }


    public void cargarFragmentoAñadir() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.añadir, new AnadirFragmento()); // AñadirFragmento es el fragmento que quieres cargar.
        transaction.addToBackStack(null);  // Para que el usuario pueda volver al fragmento anterior.
        transaction.commit();
    }

    public void cargarFragmentoEditar() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.editar, new EditarFragmento()); // EditarFragmento es el fragmento que quieres cargar.
        transaction.addToBackStack(null);  // Para que el usuario pueda volver al fragmento anterior.
        transaction.commit();
    }

}