package com.example.weeking.workers.fragmentos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.Manifest;
import android.widget.Toast;


import com.example.weeking.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class camarafragmento extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback,EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private ImageView imageView;
    private Button btPick;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    public camarafragmento() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camarafragmento, container, false);


        imageView = view.findViewById(R.id.imageView);
        btPick = view.findViewById(R.id.bt_pick);


        btPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });


        return view;
    }
    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void requestCameraPermission() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            takePicture();
        } else {
            EasyPermissions.requestPermissions(this, "Necesitamos permisos de cámara para tomar fotos",
                    REQUEST_CAMERA_PERMISSION, perms);
        }
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            takePicture();
        }
    }
    @Override
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
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {

            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(imageBitmap);
            int newWidth = 700; // Ancho deseado en píxeles
            int newHeight = 1000; // Alto deseado en píxeles
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, newWidth, newHeight, true);

            imageView.setImageBitmap(scaledBitmap);


        }
    }

}