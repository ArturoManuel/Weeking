package com.example.weeking.workers;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.R;
import com.example.weeking.databinding.ActivityNuevaActividadBinding;
import com.example.weeking.entity.Actividad;
import com.example.weeking.workers.fragmentos.ListaFragmento;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class NuevaActividadActivity extends AppCompatActivity {
    ActivityNuevaActividadBinding binding;
    private static final int REQUEST_PICK_IMAGE = 1;
    private boolean imagenSeleccionada = false;
    private Uri imagenSeleccionadaUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNuevaActividadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar el título del TextView que actúa como título del Toolbar
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Crear Actividad");
        }

        binding.btnSaveActivity.setOnClickListener(v -> {
            String nombre = binding.nombreActividad.getText().toString().trim();
            String descripcion = binding.descriptionEditText.getText().toString().trim();

            // Validación de campos vacíos
            if (nombre.isEmpty() || descripcion.isEmpty() || !imagenSeleccionada) {
                Toast.makeText(NuevaActividadActivity.this, "Por favor, complete todos los campos e incluya una imagen", Toast.LENGTH_SHORT).show();
                return;
            }

            // Deshabilitar el botón de guardar
            binding.btnSaveActivity.setEnabled(false);

            crearNuevaActividad(nombre, descripcion, new ArrayList<>());
            // No llamar a navigateToActivity ni a finish aquí
        });


        binding.uploadButton.setOnClickListener(v->{abrirGaleria();});
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null){
            imagenSeleccionadaUri = data.getData();
            if (imagenSeleccionadaUri != null) {
                binding.uploadedImage.setImageURI(imagenSeleccionadaUri);
                binding.imagenCargar.setText("");
                imagenSeleccionada = true;
            }
        }
    }


    private void abrirGaleria() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE);
    }

    private void navigateToActivity(Class<?> destinationClass) {
        Intent intent = new Intent(NuevaActividadActivity.this, destinationClass);
        startActivity(intent);
    }
    private void crearNuevaActividad(String nombre, String descripcion, List<String> listaEventosIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference actividadRef = db.collection("activity");

        Actividad actividad = new Actividad();
        actividad.setNombre(nombre);
        actividad.setDescripcion(descripcion);
        actividad.setSeguidores(0);
        actividad.setListaEventosIds(new ArrayList<>());
        actividad.setImagenUrl(""); // Inicialmente vacío

        // Agregar la actividad a Firestore y obtener el ID
        actividadRef.add(actividad)
                .addOnSuccessListener(documentReference -> {
                    String idActividad = documentReference.getId();
                    actividad.setId(idActividad);
                    // Dado que la imagen es obligatoria, siempre llamar a este método
                    subirImagenYActualizarActividad(idActividad, actividad, documentReference);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error añadiendo el documento", e);
                    // Volver a habilitar el botón de guardar si falla la creación
                    binding.btnSaveActivity.setEnabled(true);
                });
    }





    private void subirImagenYActualizarActividad(String idActividad, Actividad actividad, DocumentReference actividadDocRef) {
        String path = "actividades/" + idActividad + "/image.jpg";
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(path);

        fileRef.putFile(imagenSeleccionadaUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            actividad.setImagenUrl(uri.toString());
                            actividadDocRef.set(actividad)
                                    .addOnSuccessListener(aVoid -> {
                                        navigateToActivity(ActividadesActivity.class);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(NuevaActividadActivity.this, "Error al actualizar la actividad: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        binding.btnSaveActivity.setEnabled(true);
                                    });
                        }))
                .addOnFailureListener(e -> {
                    Toast.makeText(NuevaActividadActivity.this, "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    binding.btnSaveActivity.setEnabled(true);
                });
    }


}
