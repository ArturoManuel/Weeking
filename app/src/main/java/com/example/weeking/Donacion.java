package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class Donacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donacion);

        RadioButton tranferencia = findViewById(R.id.transferencia);
        RadioButton plin = findViewById(R.id.plin);
        RadioButton yape = findViewById(R.id.yape);
        Button boton = findViewById(R.id.button8);

        if(tranferencia.isChecked()){
            boton.setOnClickListener(view ->{
                Intent intent= new Intent(Donacion.this,Transferencia.class);
                startActivity(intent);
            });
        }else if(plin.isChecked()){
            boton.setOnClickListener(view ->{
                Intent intent= new Intent(Donacion.this,Plin.class);
                startActivity(intent);
            });
        } else if (yape.isChecked()) {
            boton.setOnClickListener(view ->{
                Intent intent= new Intent(Donacion.this,Yape.class);
                startActivity(intent);
            });
        }else{
            Toast.makeText(this, "No has seleccionado ninguno", Toast.LENGTH_SHORT).show();
        }

    }


}