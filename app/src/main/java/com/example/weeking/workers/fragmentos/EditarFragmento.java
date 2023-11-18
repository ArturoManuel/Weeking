package com.example.weeking.workers.fragmentos;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weeking.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

public class EditarFragmento extends Fragment {

    private String idActividad;
    private String nombreActividad;
    private String descripcionActividad;

    private  String imagen;

    // Variables para representar los componentes de la vista.
    private EditText nombreActividadEditText;

    private ImageButton uploadButton;
    private ImageView uploadedImage;

    private TextView textoimagen;

    private static final int REQUEST_PICK_IMAGE = 1;
    private boolean imagenSeleccionada = false;
    private Uri imagenSeleccionadaUri;

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
        uploadedImage = view.findViewById(R.id.uploadedImage);
        textoimagen=view.findViewById(R.id.imagenCargar);    // Asegúrate de que este es el ID correcto.

        // ... (otros componentes de edición)

        // Recupera los argumentos pasados al fragmento.
        if (getArguments() != null) {
            idActividad = getArguments().getString("idActividad");
            nombreActividad = getArguments().getString("nombreActividad");
            descripcionActividad = getArguments().getString("descripcionActividad");
            imagen=getArguments().getString("imagen");
            Log.d("imagen ", imagen);

            // ... (recupera otros datos si los has pasado)

            // Llena los componentes con los datos recuperados.
            nombreActividadEditText.setText(nombreActividad);
            descripcionActividadEditText.setText(descripcionActividad);
            if (imagen != null && !imagen.isEmpty()) {
                Uri imagenUri = Uri.parse(imagen);
                if (imagen != null && !imagen.isEmpty()) {
                    Glide.with(getContext())
                            .load(imagenUri)
                            .into(uploadedImage);
                    textoimagen.setText("");
                } else {
                    Log.e("EditarFragmento", "URL de imagen es nula o vacía");
                }

            } else {
                Log.e("EditarFragmento", "URL de imagen es nula o vacía");
            }

            // ... (setea otros componentes con los datos recuperados)
        }

        Button guardarButton = view.findViewById(R.id.btnSaveActivity);

        uploadedImage.setOnClickListener(v->{abrirGaleria();});

        guardarButton.setOnClickListener(v -> actualizarDatos());



        return view;
    }


    private void abrirGaleria() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null){
            imagenSeleccionadaUri = data.getData();
            if (imagenSeleccionadaUri != null) {
                uploadedImage.setImageURI(imagenSeleccionadaUri);
                textoimagen.setText("");
                imagenSeleccionada = true;
            }
        }

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
