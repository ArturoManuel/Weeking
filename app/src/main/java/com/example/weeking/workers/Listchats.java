package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.weeking.Adapter.AdaptadorE;
import com.example.weeking.R;
import com.example.weeking.entity.ListaEven;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Listchats extends AppCompatActivity {
    MeowBottomNavigation meowBottomNavigation;
    Intent intent;
    List<ListaEven> elements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listchats);
        init();
        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
        navbarnavegation();
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
        AdaptadorE listaAdapter = new AdaptadorE(elements,this);
        RecyclerView recyclerView = findViewById(R.id.lista_chat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listaAdapter);
    }

    private void navbarnavegation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_chat);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_chat) {
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
            } else if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), VistaPrincipal.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }


            return false;
        });

    }
}