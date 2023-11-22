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

import java.io.File;
import java.io.IOException;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class GaleriaEventos extends AppCompatActivity {
    GridView gridView;

    //private ArrayList<DataClass> dataList;
    FloatingActionButton anadir, camara, galeria;
    boolean aBoolean = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_eventos);

        anadir = findViewById(R.id.flatBtnAddNewPhoto);
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
            Intent intent = new Intent(GaleriaEventos.this, GaleriaUploadActivity.class);
            startActivity(intent);
        });

        }




}