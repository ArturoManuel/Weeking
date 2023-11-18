package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.databinding.ActivityVistaEventoBinding;
import com.example.weeking.entity.EventoClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VistaEventoActivity extends AppCompatActivity {
    ActivityVistaEventoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnGaleria.setOnClickListener(view -> {
            Intent intent = new Intent(VistaEventoActivity.this, GaleriaEventos.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });


        EventoClass eventoSeleccionado = DataHolder.getInstance().getEventoSeleccionado();
        String even = (eventoSeleccionado.getNombre() != null) ? eventoSeleccionado.getNombre() : "Falta llenar el campo nombre del evento";
        String descri = (eventoSeleccionado.getDescripcion() != null) ? eventoSeleccionado.getDescripcion() : "Falta llenar el campo descripción";
        String ubica = (eventoSeleccionado.getUbicacion() != null) ? eventoSeleccionado.getUbicacion() : "Falta llenar el campo ubicación";
        String foto = eventoSeleccionado.getFoto();
        String fecha = String.valueOf(eventoSeleccionado.getFecha_evento());
        String  latitud = String.valueOf(eventoSeleccionado.getLatitud());
        String longitud = String.valueOf(eventoSeleccionado.getLongitud());

        Log.d("Fecha , Latitud ,Longuitud",fecha+latitud+longitud);


        Date date = eventoSeleccionado.getFecha_evento().toDate();
        // Formatear la fecha
        SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault());


        String formattedDate = sdfDate.format(date); // Obtiene solo la fecha
         // Obtiene solo la hora

// Capitaliza la fecha y hora si es necesario
        formattedDate = capitalize(formattedDate);
         // Si deseas capitalizar la hora

// Establecer en los TextView correspondientes

        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String formattedTime = sdfTime.format(date); // "HH" para formato de 24 horas, "hh" para formato de 12 horas
        binding.horaEvento.setText(formattedTime);


        binding.fechaEvento.setText(formattedDate);
        binding.horaEvento.setText(formattedTime);

        binding.nombreEvento.setText(even);
        binding.descripciontext.setText(descri);
        binding.ubicacion.setText(ubica);

        if (foto != null && !foto.isEmpty()) {
            Glide.with(this)
                    .load(foto)
                    .into(binding.imagenEvento);
        }


        binding.btnMapa.setOnClickListener(view -> {
            // Asegúrate de tener latitud y longitud del destino
            String destinationLatitude = latitud;
            String destinationLongitude = longitud;
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destinationLatitude + "," + destinationLongitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(VistaEventoActivity.this, "Google Maps no está instalado.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public String capitalize(String str) {
        // Verifica si la cadena está vacía o es nula.
        if(str == null || str.isEmpty()) {
            return str;
        }
        // Divide la cadena en palabras.
        String[] words = str.split(" ");
        StringBuilder sb = new StringBuilder();

        // Itera a través de cada palabra.
        for (String word : words) {
            // Convierte el primer carácter de cada palabra a mayúsculas.
            sb.append(Character.toUpperCase(word.charAt(0)));
            // Añade el resto de la palabra en minúsculas.
            sb.append(word.substring(1).toLowerCase());
            // Añade un espacio después de cada palabra.
            sb.append(" ");
        }

        // Elimina el espacio adicional al final y devuelve la cadena resultante.
        return sb.toString().trim();
    }
}