package com.example.weeking.workers.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.weeking.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class mapafragmento extends Fragment implements OnMapReadyCallback {

    public interface OnMapReadyListener {
        void onMapReady();
    }

    private OnMapReadyListener callback;
    private GoogleMap mMap;
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

        // Ubicación por defecto
        LatLng defaultLocation = new LatLng(-12.072257, -77.079859);
        mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Ubicación por defecto"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));

        // Agrega otros puntos/marcadores en el mapa
        LatLng point1 = new LatLng(-12.070000, -77.080000);
        mMap.addMarker(new MarkerOptions().position(point1).title("Punto 1"));

        LatLng point2 = new LatLng(-12.075000, -77.078000);
        mMap.addMarker(new MarkerOptions().position(point2).title("Punto 2"));


        LatLng pabellonV = new LatLng(-12.073063974496803, -77.08186661970869);
        mMap.addMarker(new MarkerOptions().position(pabellonV).title("Pabellón V"));

        LatLng canchaDeMinas = new LatLng(-12.072174128386596, -77.08199877048409);
        mMap.addMarker(new MarkerOptions().position(canchaDeMinas).title("Cancha de Minas"));


        LatLng polideportivo = new LatLng(-12.066550114711053, -77.08026020484829);
        mMap.addMarker(new MarkerOptions().position(polideportivo).title("Polideportivo"));


        LatLng facultadCienciasIngenieria = new LatLng(-12.07252824709505, -77.07943635537202);
        mMap.addMarker(new MarkerOptions().position(facultadCienciasIngenieria).title("Facultad de Ciencias e Ingeniería"));


        LatLng ajedrezPUCP = new LatLng(-12.066682180584055, -77.07993397133318);
        mMap.addMarker(new MarkerOptions().position(ajedrezPUCP).title("Ajedrez PUCP"));


        LatLng tenisDeMesaPUCP = new LatLng(-12.066921524250779, -77.080345690413);
        mMap.addMarker(new MarkerOptions().position(tenisDeMesaPUCP).title("Tenis de Mesa PUCP"));

        if (callback != null) {
            callback.onMapReady();
        }



    }
}