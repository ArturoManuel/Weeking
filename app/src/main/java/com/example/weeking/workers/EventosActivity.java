package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.weeking.Adapter.AdaptadorE;
import com.example.weeking.Adapter.DelegadoActividadesAdapter;
import com.example.weeking.R;
import com.example.weeking.entity.Actividad;
import com.example.weeking.entity.ListaEven;
import com.example.weeking.workers.fragmentos.EditarDelegadoActividad;
import com.example.weeking.workers.viewModels.AppViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventosActivity extends AppCompatActivity {
    private DelegadoActividadesAdapter listaAdapter;
    private RecyclerView recyclerView;
    private AppViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        recyclerView = findViewById(R.id.listareEven);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaAdapter = new DelegadoActividadesAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(listaAdapter);

        // Configurar el listener del clic en el adaptador
        listaAdapter.setOnActividadClickListener(new DelegadoActividadesAdapter.OnActividadClickListener() {
            @Override
            public void onActividadClick(Actividad actividad) {
                mostrarDetalleDeActividad(actividad);
            }
        });

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Observar los cambios en el LiveData de las actividades
        viewModel.getListaActividades().observe(this, this::updateUI);

        // Cargar datos de Firestore a través del ViewModel
        viewModel.cargarDatosDeFirestore(FirebaseFirestore.getInstance());
    }


    private void mostrarDetalleDeActividad(Actividad actividad) {
        EditarDelegadoActividad editarFragment = new EditarDelegadoActividad();
        Bundle args = new Bundle();

        // Pasar la instancia serializable de Actividad al fragmento.
        args.putSerializable("actividad", actividad); // Usa "actividad" como clave o cualquier otra clave que prefieras.

        editarFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editarFragment)
                .addToBackStack(null)
                .commit();
    }

    private void updateUI(List<Actividad> actividades) {
        // Actualiza el adaptador con los nuevos datos
        listaAdapter.setActividades(actividades);
        listaAdapter.notifyDataSetChanged();
    }
}
