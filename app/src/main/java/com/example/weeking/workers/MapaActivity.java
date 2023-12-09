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
import com.example.weeking.entity.LugarPUCP;
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

import java.util.Arrays;
import java.util.List;

public class MapaActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Marker miMarcador;
    private final int LOCATION_REQUEST_CODE = 1234;

    private List<LugarPUCP> lugaresPUCP = Arrays.asList(
            new LugarPUCP("Estudios Generales Ciencias", new LatLng(-12.067568451831445, -77.08019614219666)),
            new LugarPUCP("Departamento de Educación", new LatLng(-12.067497632527369, -77.07939147949219)),
            new LugarPUCP("Complejo Mac Gregor", new LatLng(-12.068452380089248, -77.07850098609924)),
            new LugarPUCP("Facultad de Arte", new LatLng(-12.068754016661332, -77.08050727844238)),
            new LugarPUCP("Polideportivo", new LatLng(-12.066550114711053, -77.08026020484829)),
            new LugarPUCP("Pabellón V", new LatLng(-12.073063974496803, -77.08186661970869)),
            new LugarPUCP("Cancha de Minas", new LatLng(-12.072174128386596, -77.08199877048409)),
            new LugarPUCP("Facultad de Ciencias e Ingeniería", new LatLng(-12.07252824709505, -77.07943635537202)),
            new LugarPUCP("Ajedrez PUCP",new LatLng(-12.066682180584055, -77.07993397133318)),
            new LugarPUCP("Tenis de Mesa PUCP",new LatLng(-12.066921524250779, -77.080345690413)),
            new LugarPUCP("Edificio H", new LatLng(-12.069616957675034, -77.08140850067139))
            // Continúa añadiendo más lugares si es necesario
    );




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
            for (LugarPUCP lugar : lugaresPUCP) {
                mMap.addMarker(new MarkerOptions()
                        .position(lugar.getCoordenadas())
                        .title(lugar.getNombre()));

            }

            // Solicitar permisos y configurar la ubicación actual
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                return;
            }

            mMap.setMyLocationEnabled(true);
            setMarcadorEnMiPosicion();

            // Configurar el OnMarkerClickListener aquí
            mMap.setOnMarkerClickListener(marker -> {
                posicionSeleccionada = marker.getPosition();
                nombreMarcador = marker.getTitle();

                mostrarDialogoDeConfirmacion(posicionSeleccionada, nombreMarcador);
                return true;
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
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miPosicion, 15));
                    }
                });
    }


    private void mostrarDialogoDeConfirmacion(LatLng posicionSeleccionada, String nombreMarcador) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Lugar");
        builder.setMessage("¿Quieres seleccionar este lugar: " + nombreMarcador + "?");

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedPoint", posicionSeleccionada);
            resultIntent.putExtra("selectedName", nombreMarcador);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            // No es necesario hacer nada aquí, simplemente se cierra el diálogo
            dialog.cancel();
        });

        builder.show();
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

