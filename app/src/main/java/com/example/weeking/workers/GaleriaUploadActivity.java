package com.example.weeking.workers;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.entity.EventoClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    String eventoID;
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
        EventoClass eventoSeleccionado = DataHolder.getInstance().getEventoSeleccionado();
        eventoID = eventoSeleccionado.getEventId();
        imgCargar.setOnClickListener(view -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        cargaGaleria.setOnClickListener(view -> {
            Log.d("msg-test", String.valueOf(imageUri));
            uploadToFireStorage(imageUri);
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
            Log.d("msg-test", String.valueOf(imageUri));
            imgCargar.setImageBitmap(originalBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void uploadToFireStorage(Uri uri) {
        StorageReference imgRef = reference.child("eventos/" + eventoID + "/" + uri.getLastPathSegment());
        UploadTask uploadTask = imgRef.putFile(uri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                e.printStackTrace();
                Toast.makeText(GaleriaUploadActivity.this, "Algo pasó. Inténtelo de nuevo", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(GaleriaUploadActivity.this, "Archivo subido correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GaleriaUploadActivity.this, GaleriaEventos.class);
                startActivity(intent);
                finish();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
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
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(
            new ActivityResultContracts.PickVisualMedia(),
            uri -> {
                if (uri != null){
                    Log.d("msg-test", "Uri = "+uri);
                    Log.d("msg-test",eventoID);
                    Log.d("msg-test", uri.getLastPathSegment());
                    mostrarImagenEnImageView(uri);
                    imageUri = uri;
                        /**/
                }else{
                    Log.d("msg-test", "Uri nula");
                }
            }
    );
}