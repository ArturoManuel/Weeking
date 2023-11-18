package com.example.weeking.workers.fragmentos;

import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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

    private ImageView imagenactividad;

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
        imagenactividad= view.findViewById(R.id.imagenActividad);
        actividadSeleccionada = DataHolder.getInstance().getActividadSeleccionada();
        añadir = view.findViewById(R.id.btn_add);
        Log.d("idActividadSelecionada",actividadSeleccionada.getId());
        Log.d("url",actividadSeleccionada.getImagenUrl());
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

            Glide.with(getContext())
                    .load(actividadSeleccionada.getImagenUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("InfoFragmento", "Error al cargar imagen: ", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imagenactividad);
        } else {
            Log.e("InfoFragmento", "actividadSeleccionada is null");
        }
    }

}
