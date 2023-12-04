package com.example.weeking.workers;

import static android.content.ContentValues.TAG;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.weeking.R;
import com.example.weeking.databinding.ActivityActividadesBinding;
import com.example.weeking.entity.Actividad;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.LIstaAct;
import com.example.weeking.workers.viewModels.AppViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActividadesActivity extends AppCompatActivity {
    ActivityActividadesBinding binding;

    FirebaseFirestore db;
    AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActividadesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        View toolbarView = inflater.inflate(R.layout.toolbar_layout, binding.toolbar, false);


        TextView toolbarTitle = toolbarView.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(" Lista de Actividades");

        binding.toolbar.addView(toolbarView);



        // Inicializa el ViewModel
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        binding.btnAddActivity.setOnClickListener(v -> {
            Intent intent = new Intent(ActividadesActivity.this, NuevaActividadActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
        // Inicializa la conexión a Firestore
        db = FirebaseFirestore.getInstance();
        // Carga los datos usando el ViewModel
        appViewModel.cargarDatosDeFirestore(db);
    }



}





