package com.example.weeking.workers;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.weeking.R;
import com.example.weeking.workers.adaptador.GaleriaFotosAdapter;

public class imagen_completa extends AppCompatActivity {

    GaleriaFotosAdapter galeriaFotosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen_completa);
        ImageView imageView = (ImageView) findViewById(R.id.iv_foto);
        ActionBar actionBar=getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Imagen");
        }
        Intent intent=getIntent();

        int posicion = intent.getExtras().getInt("misImagenes");
        //galeriaFotosAdapter=new GaleriaFotosAdapter(this);
        imageView.setImageResource(galeriaFotosAdapter.imageArray[posicion]);
    }
}