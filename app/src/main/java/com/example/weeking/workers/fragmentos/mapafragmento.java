package com.example.weeking.workers.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.weeking.R;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.ListaDon;
import com.example.weeking.entity.ListaEven;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        db.collection("Eventos").addSnapshotListener((collection, error) -> {
            if (error != null) {
                Log.d("lectura", "Error listening for document changes.");
                return;
            }
            if (collection != null && !collection.isEmpty()) {
                Log.d("asdf","a");
                for (QueryDocumentSnapshot document : collection) {
                    EventoClass dona = document.toObject(EventoClass.class);
                    LatLng a = new LatLng(dona.getLatitud(), dona.getLongitud());
                    Marker marker = mMap.addMarker(new MarkerOptions().position(a).title(dona.getDescripcion()));
                    if (marker != null) {
                        marker.showInfoWindow();
                    }
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