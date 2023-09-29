package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class PerfilActivity extends AppCompatActivity {
    MeowBottomNavigation meowBottomNavigation;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());

        meowBottomNavigation = findViewById(R.id.bottomNavigation);
        meowBottomNavigation.show(1,true);
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
                    case 5:
                        intent = new Intent(PerfilActivity.this, VistaPrincipal.class);
                        startActivity(intent);
                        meowBottomNavigation.show(1,false);
                        break;
                    case 2:
                        intent = new Intent(PerfilActivity.this, Listchats.class);
                        startActivity(intent);
                        meowBottomNavigation.show(2,false);
                        break;
                    case 3:
                        intent = new Intent(PerfilActivity.this, MapaActivity.class);
                        startActivity(intent);
                        meowBottomNavigation.show(3,false);
                        break;
                    case 4:
                        Toast toast = Toast.makeText(PerfilActivity.this, "Todavía no realizamos  la camara", Toast.LENGTH_LONG);
                        toast.show();
                        meowBottomNavigation.show(4,false);
                        break;
                }
                return null;
            }
        });
    }
}