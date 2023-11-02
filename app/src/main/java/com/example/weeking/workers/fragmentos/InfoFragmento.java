package com.example.weeking.workers.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.entity.Actividad;
import com.example.weeking.workers.ActividadActivity;


public class InfoFragmento extends Fragment {

    // Variables para representar los componentes de la vista, por ejemplo TextViews.
    private TextView nombreActividadTextView;

    private TextView descripcionText;
    // ... (otros componentes para mostrar la información de la actividad)

    private ImageView añadir;

    private  ImageView editar;
    public InfoFragmento() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_fragmento, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nombreActividadTextView = view.findViewById(R.id.nombreActividad);
        descripcionText = view.findViewById(R.id.descriptionTextView);

        añadir = view.findViewById(R.id.btn_add);
        añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof ActividadActivity) {
                    ((ActividadActivity) getActivity()).cargarFragmentoAñadir();
                }
            }
        });

        editar = view.findViewById(R.id.btn_edit);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof ActividadActivity) {
                    ((ActividadActivity) getActivity()).cargarFragmentoEditar();
                }
            }
        });


        cargarDatosActividad();
    }

    private void cargarDatosActividad() {
        Actividad actividadSeleccionada = DataHolder.getInstance().getActividadSeleccionada();
        if (actividadSeleccionada != null) {
            nombreActividadTextView.setText(actividadSeleccionada.getNombre());
            descripcionText.setText(actividadSeleccionada.getDescripcion());
        }
    }
}
