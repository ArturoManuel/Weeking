package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.example.weeking.Adapter.AdaptadorE;
import com.example.weeking.R;
import com.example.weeking.databinding.ActivityListchatsBinding;
import com.example.weeking.entity.ListaEven;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Listchats extends AppCompatActivity {

    Intent intent;
    List<ListaEven> elements;
    ActivityListchatsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listchats);

    }



}