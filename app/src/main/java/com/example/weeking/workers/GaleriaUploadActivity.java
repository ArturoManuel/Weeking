package com.example.weeking.workers;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.weeking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class GaleriaUploadActivity extends AppCompatActivity {
    ImageView imgCargar;
    EditText descripcion;
    Button cargaGaleria;
    ProgressBar progressBar;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference reference;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_upload);
        imgCargar = findViewById(R.id.imgFotoCargar);
        descripcion = findViewById(R.id.descripcion);
        cargaGaleria = findViewById(R.id.cargaImagenGaleria);
        progressBar = findViewById(R.id.progresoCarga);
        progressBar.setVisibility(View.INVISIBLE);
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imageUri = data.getData();
                        imgCargar.setImageURI(imageUri);
                    } else {
                        Toast.makeText(GaleriaUploadActivity.this, "No se seleccionó imagen", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        imgCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE);
            }
        });
        cargaGaleria.setOnClickListener(view -> {
            if (imageUri != null){
                uploadToFireStorage(imageUri);
                Toast.makeText(GaleriaUploadActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GaleriaUploadActivity.this, GaleriaEventos.class);
                startActivity(intent);
                finish();
            } else  {
                Toast.makeText(GaleriaUploadActivity.this, "Seleccione una imagen", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE);
    }


    private void mostrarImagenEnImageView(Uri imageUri) {
        try {
            Context context = GaleriaUploadActivity.this;
            ContentResolver contentResolver = context.getContentResolver();
            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
            //int targetWidth = 700;
            //int targetHeight = 700;
            //Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true);
            imgCargar.setImageBitmap(originalBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void uploadToFireStorage(Uri uri){
        //ver esa función para guardarla en cada carpeta del evento.
        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("eventos").whereEqualTo("authUID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        Map<String, Object> datos = new HashMap<>();
        datos.put("imagen_url", urlImagen);
        db.collection("usuarios").document(Fire)
                .update(datos)
                .addOnSuccessListener(documentReference -> {
                })
                .addOnFailureListener(e -> {
                });*/
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == GaleriaUploadActivity.this.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imgCargar.setImageURI(selectedImageUri);
            mostrarImagenEnImageView(selectedImageUri);
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == GaleriaUploadActivity.this.RESULT_OK) {

            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            imgCargar.setImageBitmap(imageBitmap);
            int newWidth = 700; // Ancho deseado en píxeles
            int newHeight = 1000; // Alto deseado en píxeles
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, newWidth, newHeight, true);
            imgCargar.setImageBitmap(scaledBitmap);


        } else if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO && resultCode == GaleriaUploadActivity.this.RESULT_OK && data != null) {

            Uri selectedImageUri = data.getData();
            imgCargar.setImageURI(selectedImageUri);
        }
    }
}