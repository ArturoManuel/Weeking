package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.entity.ListaDon;
import com.example.weeking.entity.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;

public class alu extends AppCompatActivity {

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alu);
        db = FirebaseFirestore.getInstance();
        TextView nombre, codigo, estado, correo;
        ImageView imagen;
        Button boton = findViewById(R.id.boton);
        nombre = findViewById(R.id.nombre);
        codigo = findViewById(R.id.codigo);
        estado = findViewById(R.id.estado);
        correo = findViewById(R.id.correo);
        imagen = findViewById(R.id.foto);

        Usuario usuario = DataHolder.getInstance().getUsuarioseleccionado();
        nombre.setText(usuario.getNombre());
        codigo.setText(usuario.getCodigo());
        estado.setText(usuario.getEstado());
        correo.setText(usuario.getCorreo());
        if(usuario.getImagen_url().equals("tu_url_por_defecto_aqui")){
            Picasso.get().load("https://cdn-icons-png.flaticon.com/512/3135/3135768.png").into(imagen);
        }else {
            Picasso.get().load(usuario.getImagen_url()).into(imagen);
        }
        if(usuario.getBan().equals("0")){
            boton.setText("Habilitar");
            boton.setBackgroundColor(Color.GREEN);
        }
        boton.setOnClickListener(view -> {
            if(usuario.getBan().equals("0")){
                HashMap<String, Object> map = new HashMap<>();
                map.put("ban","1");
                db.collection("usuarios").document(usuario.getCodigo()).update(map);
                finish();
            }else if(usuario.getRol().equals("delegado_de_actividad")){
                Toast.makeText(this, "no puedes deshabilitar a un delegado de actividad, primero quitale el cargo.", Toast.LENGTH_SHORT).show();
            }else {
                HashMap<String, Object> map = new HashMap<>();
                map.put("ban","0");
                db.collection("usuarios").document(usuario.getCodigo()).update(map);
                finish();
            }
        });
    }
}