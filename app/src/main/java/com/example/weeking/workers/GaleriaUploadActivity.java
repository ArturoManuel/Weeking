package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.example.weeking.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class GaleriaUploadActivity extends AppCompatActivity {
    ImageView imgCargar;
    EditText descripcion;
    Button cargaGaleria;
    Uri imageUri;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_upload);
        imgCargar = findViewById(R.id.imgFotoCargar);
        descripcion = findViewById(R.id.descripcion);
        cargaGaleria = findViewById(R.id.cargaImagenGaleria);
        imgCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        cargaGaleria.setOnClickListener(view -> {
            if (imageUri != null){
                //uploadToFirebase(imageUri);
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

            int targetWidth = 700;
            int targetHeight = 700;

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true);

            imgCargar.setImageBitmap(scaledBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
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