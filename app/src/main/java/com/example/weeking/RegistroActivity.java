package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterViewAnimator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {

    String[] items ={"Egresado","Estudiante"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        TextView text = findViewById(R.id.tienescuenta);
        Button button = findViewById(R.id.btnRegister);
        TextView volver =findViewById(R.id.volver);
        volver.setOnClickListener(v -> finish());
       // función para mostrar los items de estados;
        setItems(autoCompleteTextView);

        text.setOnClickListener(v -> navigateToActivity(MainActivity.class));
        button.setOnClickListener(v -> navigateToActivity(VistaPrincipal.class));



    }

    private void navigateToActivity(Class<?> destinationClass) {
        Intent intent = new Intent(RegistroActivity.this, destinationClass);
        startActivity(intent);
        finish();
    }

    private void setItems( AutoCompleteTextView textView){
        textView = findViewById(R.id.estados);
        adapter = new ArrayAdapter<String>(this,R.layout.linear_items,items);
        textView.setAdapter(adapter);
    }


}