package com.example.weeking.workers;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.weeking.R;
import com.example.weeking.workers.adaptador.GaleriaFotosAdapter;
import com.example.weeking.workers.fragmentos.camarafragmento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class GaleriaEventos extends AppCompatActivity {
    GridView gridView;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    //private ArrayList<DataClass> dataList;
    FloatingActionButton anadir, camara, galeria;
    boolean aBoolean = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_eventos);

        anadir = findViewById(R.id.flatBtnAddNewPhoto);
        camara = findViewById(R.id.flatBtnTakePhoto);
        galeria = findViewById(R.id.flatBtnGalleryPhoto);
        gridView=(GridView) findViewById(R.id.gv_imagenes);
        gridView.setAdapter(new GaleriaFotosAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), imagen_completa.class);
                intent.putExtra("misImagenes",position);
                startActivity(intent);

            }
        });

        anadir.setOnClickListener(view -> {
            if(aBoolean){

                galeria.hide();
                camara.hide();
                aBoolean = false;
            } else {
                galeria.show();
                camara.show();
                aBoolean = true;
            }
        });
        camara.setOnClickListener(view -> requestCameraPermission());
        galeria.setOnClickListener(view -> {
            Intent intent = new Intent(GaleriaEventos.this, GaleriaUploadActivity.class);
            startActivity(intent);
        });

        }

    /*private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE);
    }*/



    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(GaleriaEventos.this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    /*private void pickImageFromGallery() {

        FilePickerBuilder.getInstance()
                .setMaxCount(1)
                .pickPhoto(this);
    }*/


    private void requestCameraPermission() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            takePicture();
        } else {
            EasyPermissions.requestPermissions(this, "Necesitamos permisos de cámara para tomar fotos",
                    REQUEST_CAMERA_PERMISSION, perms);
        }
    }
    /*private void requestGalleryPermission() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            pickImageFromGallery();
        } else {
            EasyPermissions.requestPermissions(this, "Necesitamos permisos de lectura de almacenamiento para acceder a la galería", REQUEST_GALLERY_PERMISSION, perms);
        }
    }*/

    /*@Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            takePicture();
        }
        if (requestCode == REQUEST_GALLERY_PERMISSION) {
            pickImageFromGallery();
        }
    }*/
    /*@Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this)
                        .setTitle("Permisos necesarios")
                        .setRationale("Esta aplicación necesita permisos de cámara para tomar fotos.")
                        .build()
                        .show();
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_GALLERY_PERMISSION) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this)
                        .setTitle("Permisos necesarios")
                        .setRationale("Esta aplicación necesita permisos de lectura de almacenamiento para acceder a la galería.")
                        .build()
                        .show();
            } else {
                Toast.makeText(requireContext(), "Permiso de lectura de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
    /*private void mostrarImagenEnImageView(Uri imageUri) {
        try {
            Context context = requireActivity();
            ContentResolver contentResolver = context.getContentResolver();

            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);

            int targetWidth = 700;
            int targetHeight = 700;

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true);

            imageView.setImageBitmap(scaledBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}