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
    private String idActividad;

    private Actividad actividadSeleccionada = DataHolder.getInstance().getActividadSeleccionada();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActividadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("msg", actividadSeleccionada.getId().toString());

        ListaFragmento fragmentoEventos = new ListaFragmento(actividadSeleccionada.getId().toString());

        binding.addEvent.setOnClickListener(v -> {

            idActividad = actividadSeleccionada.getId().toString();
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


    public void cargarFragmentoAnadir() {
        AnadirFragmento fragmento = new AnadirFragmento();

        // Crear un Bundle.
        Bundle args = new Bundle();
        args.putString("idActividad", actividadSeleccionada.getId().toString());

        Log.d("id en la activida",actividadSeleccionada.getId().toString());
        fragmento.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.añadir, fragmento);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void cargarFragmentoEditar() {
        EditarFragmento fragmentoEditar = new EditarFragmento();

        // Crear un Bundle para pasar datos.
        Bundle args = new Bundle();
        args.putString("idActividad", actividadSeleccionada.getId());
        args.putString("nombreActividad", actividadSeleccionada.getNombre());
        args.putString("descripcionActividad", actividadSeleccionada.getDescripcion());

        args.putString("imagen",actividadSeleccionada.getImagenUrl());
        // ... (añade otros datos si los necesitas)

        fragmentoEditar.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.editar, fragmentoEditar);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}