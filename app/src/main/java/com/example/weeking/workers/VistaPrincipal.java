package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import com.example.weeking.Adapter.AdaptadorE;
import com.example.weeking.Adapter.AdaptadorPrin;
import com.example.weeking.R;
import com.example.weeking.entity.ListaEven;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class VistaPrincipal extends AppCompatActivity {

    private Intent intent;
    private LinearLayout layout;

    private List<ListaEven> elements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal);
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        setupImageSlider(imageSlider);
        init();
        navbarnavegation();


    }
    private void navbarnavegation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                return true;
            } else if (item.getItemId() == R.id.bottom_camara) {
                Toast toast = Toast.makeText(getApplicationContext(), "Todavía no realizamos la cámara", Toast.LENGTH_LONG);
                toast.show();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_maps) {
                startActivity(new Intent(getApplicationContext(), MapaActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_chat) {
                startActivity(new Intent(getApplicationContext(), Listchats.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });

    }


    private void setupImageSlider(ImageSlider imageSlider) {
        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel("https://drive.google.com/file/d/1Zna5-06QK4mboQ3nVOHBOs-dBf_HuR_N/view?usp=drive_link", "Inauguran el “XVII Festival de Teatro Saliendo de la Caja” en el Centro Cultural PUCP", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://drive.google.com/file/d/1kaW0CsG51sfXP0JfEUgsnOesGHTzTBr4/view?usp=drive_link", "Torneos exclusivos de la fibra", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://drive.google.com/file/d/1Jpzx9V7z5PO_2sCJA5Nd60-GNm9g7kMu/view?usp=drive_link", "Sábado el baileton", ScaleTypes.CENTER_CROP));

        // Configura el ImageSlider pasado como parámetro
        imageSlider.setImageList(imageList);
    }

    public void init(){
        elements = new ArrayList<>();
        elements.add(new ListaEven("Torneo de ajedrez","a","en dos dias"));
        elements.add(new ListaEven("Torneo de Futsal","a","finalizado"));
        elements.add(new ListaEven("Torneo de Basquet","a","en un dia"));
        elements.add(new ListaEven("Torneo de Voley","a","en proceso"));
        elements.add(new ListaEven("Torneo de LOL","a","en dos dias"));
        elements.add(new ListaEven("Torneo de Dota","a","en dos dias"));
        elements.add(new ListaEven("Torneo de BIKAS","a","en dos dias"));
        AdaptadorPrin listaAdapter = new AdaptadorPrin(elements,this);
        RecyclerView recyclerView = findViewById(R.id.lista_prin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listaAdapter);
    }
}