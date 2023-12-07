package com.example.weeking.workers;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.databinding.ActivityVistaEventoBinding;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.Usuario;
import com.example.weeking.workers.viewModels.AppViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


public class VistaEventoActivity extends AppCompatActivity {
    ActivityVistaEventoBinding binding;
    private MapView mapView;
    private GoogleMap gMap;
    private String latitud;
    private String longitud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        binding.btnGaleria.setOnClickListener(view -> {
            Intent intent = new Intent(VistaEventoActivity.this, GaleriaEventos.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });


        EventoClass eventoSeleccionado = DataHolder.getInstance().getEventoSeleccionado();
        String even = (eventoSeleccionado.getNombre() != null) ? eventoSeleccionado.getNombre() : "Falta llenar el campo nombre del evento";
        String descri = (eventoSeleccionado.getDescripcion() != null) ? eventoSeleccionado.getDescripcion() : "Falta llenar el campo descripción";
        String ubica = (eventoSeleccionado.getUbicacion() != null) ? eventoSeleccionado.getUbicacion() : "Falta llenar el campo ubicación";
        String foto = eventoSeleccionado.getFoto();
        String fecha = String.valueOf(eventoSeleccionado.getFecha_evento());
        latitud = String.valueOf(eventoSeleccionado.getLatitud());
        longitud = String.valueOf(eventoSeleccionado.getLongitud());


        Log.d("Fecha , Latitud ,Longuitud",fecha+latitud+longitud);


        Date date = eventoSeleccionado.getFecha_evento().toDate();
        // Formatear la fecha
        SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault());


        String formattedDate = sdfDate.format(date); // Obtiene solo la fecha
         // Obtiene solo la hora

// Capitaliza la fecha y hora si es necesario
        formattedDate = capitalize(formattedDate);
         // Si deseas capitalizar la hora

// Establecer en los TextView correspondientes

        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String formattedTime = sdfTime.format(date); // "HH" para formato de 24 horas, "hh" para formato de 12 horas
        binding.horaEvento.setText(formattedTime);


        binding.fechaEvento.setText(formattedDate);
        binding.horaEvento.setText(formattedTime);

        binding.nombreEvento.setText(even);
        binding.descripciontext.setText(descri);
        binding.ubicacion.setText(ubica);

        if (foto != null && !foto.isEmpty()) {
            Glide.with(this)
                    .load(foto)
                    .into(binding.imagenEvento);
        }


        binding.btnMapa.setOnClickListener(view -> {
            // Asegúrate de tener latitud y longitud del destino
            String destinationLatitude = latitud;
            String destinationLongitude = longitud;
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destinationLatitude + "," + destinationLongitude + "&mode=w");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(VistaEventoActivity.this, "Google Maps no está instalado.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnApoyarEvento.setOnClickListener(v -> {
            // Si ya tenemos el userId, podemos continuar
            if (userId != null && !userId.isEmpty()) {
                verificarApoyoPrevioYMostrarDialogo(userId, eventoSeleccionado.getEventId());
            } else {
                // Manejar el caso de que no hay un userId válido
                Toast.makeText(VistaEventoActivity.this, "No se pudo obtener el identificador del usuario.", Toast.LENGTH_SHORT).show();
            }
        });
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this::onMapReady);

        if (currentUser != null) {
            // Buscar el documento basado en el campo authUID que coincide con userId
            Query query = db.collection("usuarios").whereEqualTo("authUID", userId);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0); // Asume que solo hay un documento que coincide
                        String rol = document.getString("rol"); // Asume que el campo del rol se llama "rol"
                        obtenerCodigoDelAlumno(userId);
                        if ("administrador".equals(rol)) {
                            binding.btnEditEvent.setVisibility(View.INVISIBLE);
                        } else if ("delegado_de_actividad".equals(rol)) {
                            binding.btnEditEvent.setVisibility(View.VISIBLE);
                        } else if ("alumno".equals(rol)) {
                            binding.btnEditEvent.setVisibility(View.INVISIBLE);
                        } else {
                            Log.d(TAG, "Rol no reconocido");
                            return; // Sale de la función.
                        }

                        binding.btnEditEvent.setOnClickListener(view -> {
                            Intent intent = new Intent(VistaEventoActivity.this, NuevoEventoActivity.class);
                            startActivity(intent);
                            finish();
                        });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        }

    }


    private void verificarApoyoPrevioYMostrarDialogo(String authId, String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuarios").whereEqualTo("authUID", authId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Map<String, Object> comentariosDeApoyo = (Map<String, Object>) documentSnapshot.get("comentariosDeApoyo");
                        if (comentariosDeApoyo == null || !comentariosDeApoyo.containsKey(eventId)) {
                            mostrarDialogoDeApoyo(eventId, documentSnapshot.getId());
                        } else {
                            Toast.makeText(VistaEventoActivity.this, "Ya has expresado tu apoyo para este evento.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("Firestore", "El documento del usuario no fue encontrado.");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error al verificar el apoyo del evento", e));
    }

    private void mostrarDialogoDeApoyo(String eventId, String documentId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, com.google.android.material.R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        Log.d("IdDocumento",documentId.toString());
        // Configurar el título y el mensaje del diálogo
        builder.setTitle("Formulario de apoyo");

        // Inflar la vista personalizada para el diálogo
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_suport_form, null);
        Spinner spinner = dialogView.findViewById(R.id.spinner_support_options);
        EditText inputReason = dialogView.findViewById(R.id.edittext_reason);

        // Crear un ArrayAdapter para el spinner con las opciones
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Participante", "Barra"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Establecer la vista en el diálogo
        builder.setView(dialogView);

        // Configurar los botones del diálogo
        builder.setPositiveButton("Enviar", (dialogInterface, i) -> {
            String supportType = spinner.getSelectedItem().toString();
            String reason = inputReason.getText().toString();

            // Verificar que se haya seleccionado un tipo de apoyo y que se haya escrito un comentario
            if (!supportType.isEmpty() && !reason.trim().isEmpty()) {
                // Construir el mapa con el tipo de apoyo y el comentario
                Map<String, Object> apoyo = new HashMap<>();
                apoyo.put("tipoApoyo", supportType);
                apoyo.put("comentario", reason);

                // Preparar la actualización para Firestore
                actualizarComentarioDeApoyo(documentId, eventId, apoyo);
            } else {
                Toast.makeText(VistaEventoActivity.this, "Por favor, selecciona un tipo de apoyo y escribe un comentario.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.cancel());

        // Mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void actualizarComentarioDeApoyo(String documentId, String idEvento, Map<String, Object> apoyo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Actualizar el documento del usuario con el nuevo comentario de apoyo
        db.collection("usuarios").document(documentId)
                .update("comentariosDeApoyo." + idEvento, apoyo)
                .addOnSuccessListener(aVoid -> Toast.makeText(VistaEventoActivity.this, "Muchas Gracias por Apoyar", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(VistaEventoActivity.this, "Error al actualizar el apoyo", Toast.LENGTH_SHORT).show());
    }

    private void obtenerCodigoDelAlumno(String authID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection("usuarios");

        Query query = colRef.whereEqualTo("authUID", authID);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                String codigoAlumno = document.getId();
                // Aquí obtenemos el código del alumno, que es el ID del documento
                Log.d("codigoencontrado",codigoAlumno);
                cargarDatosUsuarioDesdeFirestore(codigoAlumno);
            } else {
                Log.d("mensajeError","no se encontro el codigo");
            }
        }).addOnFailureListener(e -> {
            // Maneja cualquier error que ocurra al tratar de obtener el documento.
            Log.d("fallo en encontrar el documento","no se encontro");
        });
    }

    private void cargarDatosUsuarioDesdeFirestore(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Usuario usuario = documentSnapshot.toObject(Usuario.class);
                    if (usuario != null) {
                        // Guarda el usuario en AppViewModel
                        Log.d("datos",usuario.getNombre());
                        AppViewModel appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
                        appViewModel.setCurrentUser(usuario);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(VistaEventoActivity.this, "Error al cargar datos del usuario.", Toast.LENGTH_SHORT).show();
                });
    }


    public String capitalize(String str) {
        // Verifica si la cadena está vacía o es nula.
        if(str == null || str.isEmpty()) {
            return str;
        }
        // Divide la cadena en palabras.
        String[] words = str.split(" ");
        StringBuilder sb = new StringBuilder();

        // Itera a través de cada palabra.
        for (String word : words) {
            // Convierte el primer carácter de cada palabra a mayúsculas.
            sb.append(Character.toUpperCase(word.charAt(0)));
            // Añade el resto de la palabra en minúsculas.
            sb.append(word.substring(1).toLowerCase());
            // Añade un espacio después de cada palabra.
            sb.append(" ");
        }

        // Elimina el espacio adicional al final y devuelve la cadena resultante.
        return sb.toString().trim();
    }

    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        double lat = Double.parseDouble(latitud);
        double lng = Double.parseDouble(longitud);
        LatLng eventLocation = new LatLng(lat, lng);

        gMap.addMarker(new MarkerOptions().position(eventLocation).title("Ubicación del Evento"));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 15)); // Ajusta el zoom según sea necesario
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}