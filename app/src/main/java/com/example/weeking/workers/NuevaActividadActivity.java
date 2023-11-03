package com.example.weeking.workers;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.weeking.R;
import com.example.weeking.databinding.ActivityNuevaActividadBinding;
import com.example.weeking.entity.Actividad;
import com.example.weeking.workers.fragmentos.ListaFragmento;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NuevaActividadActivity extends AppCompatActivity {
    ActivityNuevaActividadBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNuevaActividadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSaveActivity.setOnClickListener(v->{
            String nombre = binding.nombreActividad.getText().toString();
            String descripcion = binding.descriptionEditText.getText().toString();
            crearNuevaActividad(nombre, descripcion, new ArrayList<>());
            navigateToActivity(ActividadesActivity.class);
        });

    }


    private void navigateToActivity(Class<?> destinationClass) {
        Intent intent = new Intent(NuevaActividadActivity.this, destinationClass);
        startActivity(intent);
    }
    private void crearNuevaActividad(String nombre, String descripcion, List<String> listaEventosIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference actividadRef = db.collection("activity");

        // Crear un nuevo objeto de actividad
        Actividad actividad = new Actividad();
        actividad.setNombre(nombre);
        actividad.setDescripcion(descripcion);  // Aquí establecemos la descripción
        actividad.setSeguidores(0);  // Inicializamos seguidores como 0
        actividad.setListaEventosIds(new ArrayList<>());  // Inicializamos listaEventosIds como una lista vacía

        // Agregar un campo vacío para la imagen URL
        actividad.setImagenUrl("");  // Asegúrate de que en tu clase Actividad exista este método y variable.

        // Agregar la actividad a Firestore
        actividadRef.add(actividad)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Documento añadido con ID: " + documentReference.getId());

                        // Actualizar el ID en el objeto actividad y en Firestore
                        actividad.setId(documentReference.getId());
                        actividadRef.document(documentReference.getId()).set(actividad);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error añadiendo el documento", e);
                    }
                });
    }
}
