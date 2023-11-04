package com.example.weeking.workers.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
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

    private    Actividad actividadSeleccionada;

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
        actividadSeleccionada = DataHolder.getInstance().getActividadSeleccionada();
        añadir = view.findViewById(R.id.btn_add);
        Log.d("idActividadSelecionada",actividadSeleccionada.getId());
        añadir.setOnClickListener(v -> {
            if(getActivity() instanceof ActividadActivity) {
                ((ActividadActivity) getActivity()).cargarFragmentoAnadir();
            }
        });
        editar = view.findViewById(R.id.btn_edit);
        editar.setOnClickListener(v -> {
            if(getActivity() instanceof ActividadActivity) {
                ((ActividadActivity) getActivity()).cargarFragmentoEditar();
            }
        });

        cargarDatosActividad();
    }

    private void cargarDatosActividad() {
        if (actividadSeleccionada != null) {
            if(nombreActividadTextView != null) {
                nombreActividadTextView.setText(actividadSeleccionada.getNombre());
            } else {
                Log.e("InfoFragmento", "nombreActividadTextView is null");
            }

            if(descripcionText != null) {
                descripcionText.setText(actividadSeleccionada.getDescripcion());
            } else {
                Log.e("InfoFragmento", "descripcionText is null");
            }
        } else {
            Log.e("InfoFragmento", "actividadSeleccionada is null");
        }
    }

}
