package com.example.weeking.workers.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.weeking.R;
import com.example.weeking.entity.Actividad;


public class DelegaActividadTopFragmento extends Fragment {

    private Actividad actividad;
    private ImageView imagenActividad;
    private TextView nombreActividadTextView, descripcionText;

    public DelegaActividadTopFragmento() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delega_actividad_top_fragmento, container, false);

        if (getArguments() != null) {
            actividad = (Actividad) getArguments().getSerializable("actividad");
        }

        imagenActividad = view.findViewById(R.id.imageViewActividad); // Asegúrate de que estos IDs sean correctos
        nombreActividadTextView = view.findViewById(R.id.nombreActividad);
        descripcionText = view.findViewById(R.id.descripcionActividad);

        // Configura los datos en las vistas
        if (actividad != null) {
            Glide.with(this).load(actividad.getImagenUrl()).into(imagenActividad);
            nombreActividadTextView.setText(actividad.getNombre());
            descripcionText.setText(actividad.getDescripcion());
        }

        return view;
    }
}

