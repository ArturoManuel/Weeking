package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Don_ve extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_ve);
        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
    }
}