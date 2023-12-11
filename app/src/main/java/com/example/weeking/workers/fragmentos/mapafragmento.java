package com.example.weeking.workers.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.weeking.R;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.ListaDon;
import com.example.weeking.entity.ListaEven;
import com.example.weeking.workers.viewModels.AppViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class mapafragmento extends Fragment implements OnMapReadyCallback {
    public interface OnMapReadyListener {
        void onMapReady();
    }

    private OnMapReadyListener callback;
    private GoogleMap mMap;

    private AppViewModel appViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapafragmento, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize map fragment


        view.post(() -> {
            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.mapFragment, mapFragment).commit();
            mapFragment.getMapAsync(this);
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnMapReadyListener) {
            callback = (OnMapReadyListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debe implementar OnMapReadyListener");
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float[] colorArray = {
                BitmapDescriptorFactory.HUE_RED,
                BitmapDescriptorFactory.HUE_ORANGE,
                BitmapDescriptorFactory.HUE_YELLOW,
                BitmapDescriptorFactory.HUE_GREEN,
                BitmapDescriptorFactory.HUE_CYAN,
                BitmapDescriptorFactory.HUE_AZURE,
                BitmapDescriptorFactory.HUE_BLUE,
                BitmapDescriptorFactory.HUE_VIOLET,
                BitmapDescriptorFactory.HUE_MAGENTA,

        };


        // Observa los eventos y coloca los marcadores con colores del array
        appViewModel.getListaEventos().observe(getViewLifecycleOwner(), eventoClasses -> {
            if (eventoClasses != null && !eventoClasses.isEmpty()) {
                int colorIndex = 0; // Inicializa el índice de color
                mMap.clear(); // Limpia marcadores antiguos para evitar duplicados
                for (EventoClass evento : eventoClasses) {
                    LatLng position = new LatLng(evento.getLatitud(), evento.getLongitud());
                    mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(evento.getNombre())
                            .icon(BitmapDescriptorFactory.defaultMarker(colorArray[colorIndex % colorArray.length])) // Asigna un color del array
                    );
                    colorIndex++; // Incrementa el índice para el siguiente color
                }
            }
        });

        LatLng defaultLocation = new LatLng(-12.072257, -77.079859);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));

        if (callback != null) {
            callback.onMapReady();
        }

    }
}