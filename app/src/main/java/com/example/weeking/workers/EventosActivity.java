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

    private List<Actividad> actividadesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        Log.d("AVISO","ENTROACA");
        String userId = getIntent().getStringExtra("USER_ID");
        String codigo =getIntent().getStringExtra("CODIGO");
        // Inicializar ViewModel
        viewModel = new ViewModelProvider(EventosActivity.this).get(AppViewModel.class);

        viewModel.getListaDeActividadesPorUsuario().observe(EventosActivity.this, this::updateUI);

        viewModel.cargarActividadesPorUsuario(FirebaseFirestore.getInstance(),codigo);

    }



    private void mostrarDetalleDeActividad(Actividad actividad) {
        EditarDelegadoActividad editarFragment = new EditarDelegadoActividad();
        Bundle args = new Bundle();

        // Pasar la instancia serializable de Actividad al fragmento
        args.putSerializable("actividad", actividad);

        editarFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editarFragment)
                .addToBackStack(null)
                .commit();
    }

    private void updateUI(List<Actividad> actividades) {
        actividadesList = actividades;
        mostrarDetalleDeActividad(actividadesList.get(0));
    }
    @Override
    public void onBackPressed() {
        // Finaliza la actividad actual
        finish();
    }
}
