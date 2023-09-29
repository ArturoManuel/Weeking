package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.number.Scale;
import android.os.Bundle;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.etebarian.meowbottomnavigation.MeowBottomNavigationCell;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class VistaPrincipal extends AppCompatActivity {

    MeowBottomNavigation meowBottomNavigation;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal);
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        setupImageSlider(imageSlider);
        meowBottomNavigation = findViewById(R.id.bottomNavigation);

        meowBottomNavigation.show(5,true);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.baseline_person_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.baseline_chat_bubble_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.baseline_maps_ugc_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.baseline_photo_camera_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.baseline_house_24));

        meownavegation();
    }

    private void meownavegation(){
        meowBottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
        @Override
        public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()){
                    case 1:
                        intent = new Intent(VistaPrincipal.this, PerfilActivity.class);
                        startActivity(intent);
                        meowBottomNavigation.show(1,false);
                        break;
                    case 2:
                        intent = new Intent(VistaPrincipal.this, Listchats.class);
                        startActivity(intent);
                        meowBottomNavigation.show(2,false);
                        break;
                    case 3:
                        intent = new Intent(VistaPrincipal.this, MapaActivity.class);
                        startActivity(intent);
                        meowBottomNavigation.show(3,false);
                        break;
                    case 4:
                        Toast toast = Toast.makeText(VistaPrincipal.this, "Todavía no realizamos  la camara", Toast.LENGTH_LONG);
                        toast.show();
                        meowBottomNavigation.show(4,false);
                        break;
                }
                return null;
            }
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
}