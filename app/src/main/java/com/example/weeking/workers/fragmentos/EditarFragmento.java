package com.example.weeking.workers.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weeking.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarFragmento extends Fragment {

    private String idActividad;
    private String nombreActividad;
    private String descripcionActividad;

    // Variables para representar los componentes de la vista.
    private EditText nombreActividadEditText;
    private TextInputEditText descripcionActividadEditText;
    // ... (otros componentes para editar la información de la actividad)

    public EditarFragmento() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Infla la vista.
        View view = inflater.inflate(R.layout.fragment_editar_fragmento, container, false);

        // Inicializa los componentes de la vista.
        nombreActividadEditText = view.findViewById(R.id.nombreActividad);  // Asumiendo que tienes un EditText con ese id.
        descripcionActividadEditText = view.findViewById(R.id.descriptionEditText);  // Aquí accedes a tu TextInputEditText.
        // ... (otros componentes de edición)

        // Recupera los argumentos pasados al fragmento.
        if (getArguments() != null) {
            idActividad = getArguments().getString("idActividad");
            nombreActividad = getArguments().getString("nombreActividad");
            descripcionActividad = getArguments().getString("descripcionActividad");
            // ... (recupera otros datos si los has pasado)

            // Llena los componentes con los datos recuperados.
            nombreActividadEditText.setText(nombreActividad);
            descripcionActividadEditText.setText(descripcionActividad);
            // ... (setea otros componentes con los datos recuperados)
        }

        Button guardarButton = view.findViewById(R.id.btnSaveActivity);

        guardarButton.setOnClickListener(v -> actualizarDatos());

        return view;
    }

    private void actualizarDatos() {
        // Recuperar datos editados por el usuario
        String nuevoNombre = nombreActividadEditText.getText().toString();
        String nuevaDescripcion = descripcionActividadEditText.getText().toString();

        // Acceder a Firestore y actualizar los datos
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.d("IdActividadAeditar",idActividad);

        // Suponiendo que "actividades" es el nombre de tu colección
        DocumentReference actividadRef = db.collection("activity").document(idActividad);

        // Crear un mapa con los datos a actualizar
        Map<String, Object> dataToUpdate = new HashMap<>();
        dataToUpdate.put("nombre", nuevoNombre);
        dataToUpdate.put("descripcion", nuevaDescripcion);

        actividadRef.update(dataToUpdate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Datos actualizados con éxito", Toast.LENGTH_SHORT).show();

                        // Cierra el fragmento y regresa a ActividadActivity
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error al actualizar datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("ActualizarError", "Error al actualizar datos", e);
                    }
                });

    }
}
