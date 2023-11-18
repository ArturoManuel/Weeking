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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        // Infla la vista para este fragmento
        View view = inflater.inflate(R.layout.fragment_editar_fragmento, container, false);

        // Inicializa los componentes de la vista con los IDs de tus elementos de layout
        nombreActividadEditText = view.findViewById(R.id.nombreActividad);
        descripcionActividadEditText = view.findViewById(R.id.descriptionEditText);
        uploadedImage = view.findViewById(R.id.uploadedImage);
        textoimagen = view.findViewById(R.id.imagenCargar);
        uploadButton = view.findViewById(R.id.uploadButton); // Asegúrate de que este es el ID correcto en tu layout

        // Recupera y establece los datos pasados como argumentos al fragmento
        if (getArguments() != null) {
            idActividad = getArguments().getString("idActividad");
            nombreActividad = getArguments().getString("nombreActividad");
            descripcionActividad = getArguments().getString("descripcionActividad");
            imagen = getArguments().getString("imagen");

            nombreActividadEditText.setText(nombreActividad);
            descripcionActividadEditText.setText(descripcionActividad);

            // Usa Glide para cargar la imagen si la URL no está vacía
            if (imagen != null && !imagen.isEmpty()) {
                Glide.with(this)
                        .load(imagen)
                        .into(uploadedImage);
                textoimagen.setText(""); // Limpia el texto si la imagen se carga correctamente
            } else {
                 // R.string.no_image_selected es un placeholder que indica que no hay imagen seleccionada
            }
        }

        // Asigna el evento onClickListener al botón de subida de imagen
        uploadButton.setOnClickListener(v -> abrirGaleria());

        // Asigna el evento onClickListener al botón de guardar
        Button guardarButton = view.findViewById(R.id.btnSaveActivity);
        guardarButton.setOnClickListener(v -> actualizarDatos());

        // Devuelve la vista inflada
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
                imagenSeleccionada = true; // Marca que se ha seleccionado una nueva imagen
                subirImagenYActualizarUrl(imagenSeleccionadaUri);
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


    private void subirImagenYActualizarUrl(Uri imagenUri) {
        if (imagenSeleccionada && imagenUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imagenRef = storageRef.child("actividades/" + idActividad + "/" + UUID.randomUUID().toString());

            imagenRef.putFile(imagenUri)
                    .addOnSuccessListener(taskSnapshot -> imagenRef.getDownloadUrl().addOnSuccessListener(this::actualizarImagenUrlEnFirestore))
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al subir imagen: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }

    private void actualizarImagenUrlEnFirestore(Uri downloadUri) {
        if (imagenSeleccionada) {
            String imageUrl = downloadUri.toString();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference actividadRef = db.collection("activity").document(idActividad);
            actividadRef.update("imagenUrl", imageUrl)
                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Imagen actualizada con éxito", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al actualizar imagen: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }


}
