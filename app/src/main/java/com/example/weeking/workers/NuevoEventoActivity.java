package com.example.weeking.workers;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weeking.R;
import com.example.weeking.databinding.ActivityNuevaActividadBinding;
import com.example.weeking.databinding.ActivityNuevoEventoBinding;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.workers.viewModels.AppViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class NuevoEventoActivity extends AppCompatActivity {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    private static final int MAP_REQUEST_CODE = 2;
    private static final int REQUEST_PICK_IMAGE = 3;

    private LatLng ubicacionSeleccionada;
    private String nombreUbicacion;

    private  double latitud;
    private  double longitud;

    private Uri imagenSeleccionadaUri;

    private String urlImagenExistente = null;

    private FirebaseStorage storage;
    private StorageReference storageRef;



    ActivityNuevoEventoBinding binding;
    String  idActividad;
    String idEvento;

    private boolean imagenSeleccionada = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNuevoEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        idActividad = getIntent().getStringExtra("id_actividad");
        idEvento = getIntent().getStringExtra("id_evento");


        binding.saveEventButton.setOnClickListener(v -> {
            String nombre = binding.nombreEvento.getText().toString();
            String descripcion = binding.descriptionEditText.getText().toString();
            String fecha = binding.tvSelectedDate.getText().toString();
            String hora = binding.tvSelectedTime.getText().toString();

            // Validaciones básicas de campos requeridos.
            if (nombre.isEmpty() || descripcion.isEmpty() || fecha.isEmpty() || hora.isEmpty()) {
                Toast.makeText(this, "Todos los campos deben estar llenos", Toast.LENGTH_SHORT).show();
                return;
            }

            // En caso de un evento nuevo, asegurarse de que se ha seleccionado una imagen.
            if ((idEvento == null || idEvento.isEmpty()) && imagenSeleccionadaUri == null) {
                Toast.makeText(this, "Por favor, seleccione una imagen para el evento", Toast.LENGTH_SHORT).show();
                return;
            }

            // En caso de estar editando un evento, si no hay imagen previa y no se seleccionó una nueva, mostrar error.
            if (idEvento != null && !idEvento.isEmpty() && urlImagenExistente == null && imagenSeleccionadaUri == null) {
                Toast.makeText(this, "Por favor, seleccione una imagen para el evento", Toast.LENGTH_SHORT).show();
                return;
            }

            crearOActualizarEvento(nombre, descripcion, fecha, hora);
        });


        // Cargar datos del evento si el idEvento no es nulo y no está vacío
        if (idEvento != null && !idEvento.isEmpty() ) {
            cargarDatosEvento(idEvento);
        }
        binding.lugar.setOnClickListener(v -> {
            Intent mapIntent = new Intent(NuevoEventoActivity.this, MapaActivity.class);
            startActivityForResult(mapIntent, MAP_REQUEST_CODE);
        });

        // Mostrar dialogo de fecha y hora cuando se hacen clic en los iconos respectivos
        binding.iconSelectDate.setOnClickListener(v -> mostrarDialogoFecha());
        binding.iconSelectTime.setOnClickListener(v -> mostrarDialogoHora());

        binding.uploadImageButton.setOnClickListener(v -> abrirGaleria());
    }


    private void abrirGaleria() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK) {
            LatLng selectedPoint = data.getParcelableExtra("selectedPoint");
            nombreUbicacion = data.getStringExtra("selectedName");
            if (selectedPoint != null) {
                latitud = selectedPoint.latitude;
                longitud = selectedPoint.longitude;
            }
            // Formatear la latitud y longitud con tres decimales
            String posicionFormato = String.format(Locale.getDefault(), "(%.3f;%.3f)", longitud, latitud);
            // Actualizar los TextViews con la posición y el nombre de la ubicación
            binding.punto.setText(posicionFormato);
            binding.nombreLugar.setText(nombreUbicacion);
        }else if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null){
            imagenSeleccionadaUri = data.getData();
            if (imagenSeleccionadaUri != null) {
                binding.imageViewBackground.setImageURI(imagenSeleccionadaUri);
                binding.imagenCargar.setText("");
                imagenSeleccionada = true;
            }
        }
    }



    private void mostrarDialogoFecha() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Fecha del Evento")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Convertir el tiempo en milisegundos a un objeto Calendar
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            binding.tvSelectedDate.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void mostrarDialogoHora() {
        MaterialTimePicker.Builder timePickerBuilder = new MaterialTimePicker.Builder();

        // Opcional: Configurar el formato de la hora (12 horas o 24 horas)
        timePickerBuilder.setTimeFormat(TimeFormat.CLOCK_12H);

        // Opcional: Configurar la hora inicial que se mostrará
        timePickerBuilder.setHour(12);
        timePickerBuilder.setMinute(0);

        MaterialTimePicker timePicker = timePickerBuilder.build();
        timePicker.show(getSupportFragmentManager(), "TIME_PICKER");
        timePicker.addOnPositiveButtonClickListener(dialog -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String period = hour < 12 ? "AM" : "PM";
            if (hour == 0) {
                hour = 12;
            }
            if (hour > 12) {
                hour -= 12;
            }
            binding.tvSelectedTime.setText(String.format("%02d:%02d %s", hour, minute, period));
        });
    }

    private  void cargarDatosEvento(String idEvento) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventoRef = db.collection("Eventos").document(idEvento);

        eventoRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // Aquí asignas los valores a los campos de la UI con los datos del evento
                    EventoClass evento = document.toObject(EventoClass.class);
                    if(evento != null){
                        binding.nombreEvento.setText(evento.getNombre());
                        binding.descriptionEditText.setText(evento.getDescripcion());
                        binding.nombreLugar.setText(evento.getUbicacion());
                        latitud = evento.getLatitud();
                        longitud = evento.getLongitud();
                        nombreUbicacion=evento.getUbicacion();

                        urlImagenExistente = evento.getFoto();
                        if (urlImagenExistente != null && !urlImagenExistente.isEmpty()) {
                            Glide.with(this).load(urlImagenExistente).into(binding.imageViewBackground);
                            imagenSeleccionada = false; // Importante para no subir una nueva imagen por defecto
                        }


                        String puntoFormato = String.format(Locale.getDefault(), "(%.3f;%.3f)", longitud, latitud);
                        binding.punto.setText(puntoFormato);
                        // Formato para la fecha
                        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
// Formato para la hora
                        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());

// Suponiendo que 'evento.getFecha_evento().toDate()' devuelve un objeto Date con la fecha y hora del evento
                        Date fechaEvento = evento.getFecha_evento().toDate();


                        binding.tvSelectedDate.setText(sdfDate.format(fechaEvento));

                        binding.tvSelectedTime.setText(sdfTime.format(fechaEvento));

                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }




    private void crearOActualizarEvento(String nombre, String descripcion, String fecha, String hora) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EventoClass evento = new EventoClass();
        evento.setNombre(nombre);
        evento.setDescripcion(descripcion);
        evento.setEstado(true);
        evento.setUbicacion(nombreUbicacion);
        evento.setLatitud(latitud);
        evento.setLongitud(longitud);
        evento.setIdActividad(idActividad);
        evento.setLikes(0);
        evento.setListaUsuariosIds(new ArrayList<>());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            Date fechaHora = sdf.parse(fecha + " " + hora);
            evento.setFechaEvento(new Timestamp(fechaHora));
        } catch (ParseException e) {
            e.printStackTrace();
            evento.setFechaEvento(new Timestamp(new Date()));
        }

        if (idEvento != null && !idEvento.isEmpty()) {
            // Actualizando un evento existente
            evento.setEventId(idEvento);
            if (imagenSeleccionada && imagenSeleccionadaUri != null) {
                // Subir la nueva imagen antes de actualizar el evento
                subirImagenYActualizarEvento(imagenSeleccionadaUri, evento);
            } else {
                // Mantener la imagen existente y actualizar el evento
                evento.setFoto(urlImagenExistente);
                actualizarEventoFirestore(evento);
            }
        } else {
            // Creando un nuevo evento
            DocumentReference nuevoEventoRef = db.collection("Eventos").document();
            evento.setEventId(nuevoEventoRef.getId());
            if (imagenSeleccionadaUri != null) {
                // Subir imagen antes de crear el evento
                subirImagenYActualizarEvento(imagenSeleccionadaUri, evento);
            } else {
                // Crear evento sin imagen
                actualizarEventoFirestore(evento);
            }
        }
    }

    private void subirImagenYActualizarEvento(Uri imageUri, EventoClass evento) {
        String imagenPath = "eventos/" + evento.getEventId() + "/imagen.jpg";
        StorageReference fileRef = storageRef.child(imagenPath);
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    evento.setFoto(uri.toString());
                    actualizarEventoFirestore(evento);
                }))
                .addOnFailureListener(e -> {
                    Toast.makeText(NuevoEventoActivity.this, "Error al subir imagen", Toast.LENGTH_SHORT).show();
                });
    }

    private void actualizarEventoFirestore(EventoClass evento) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Eventos").document(evento.getEventId())
                .set(evento)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(NuevoEventoActivity.this, "Evento guardado exitosamente.", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(NuevoEventoActivity.this, "Error al guardar el evento.", Toast.LENGTH_SHORT).show();
                });
    }




}