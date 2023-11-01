package com.example.weeking.workers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weeking.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Marker miMarcador;
    private final int LOCATION_REQUEST_CODE = 1234;


    private LatLng posicionSeleccionada;
    private String nombreMarcador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                return;
            }

            mMap.setMyLocationEnabled(true);
            setMarcadorEnMiPosicion();

            mMap.setOnMapClickListener(latLng -> {
                if (miMarcador != null) {
                    miMarcador.remove();
                }
                    posicionSeleccionada = latLng; // Guarda la posición seleccionada
                    mostrarDialogoParaNombre(); // Muestra el cuadro de diálogo
            });
        });
    }

    private void setMarcadorEnMiPosicion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        LatLng miPosicion = new LatLng(location.getLatitude(), location.getLongitude());
                        miMarcador = mMap.addMarker(new MarkerOptions().position(miPosicion).title("Mi Posición"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miPosicion, 15));
                    }
                });
    }

    private void mostrarDialogoParaNombre() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nombre del marcador");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            nombreMarcador = input.getText().toString();
            miMarcador = mMap.addMarker(new MarkerOptions().position(posicionSeleccionada).title(nombreMarcador));

            // Establecer el resultado y terminar la actividad
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedPoint", posicionSeleccionada);
            resultIntent.putExtra("selectedName", nombreMarcador);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public Pair<LatLng, String> obtenerMarcadorInfo() {
        return new Pair<>(posicionSeleccionada, nombreMarcador);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                setMarcadorEnMiPosicion();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

