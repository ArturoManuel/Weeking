package com.example.weeking.workers.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.weeking.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class mapafragmento extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapafragmento, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
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

        // Puedes seguir agregando más puntos según lo necesites
    }
}