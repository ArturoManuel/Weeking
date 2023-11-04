package com.example.weeking.workers;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.weeking.R;
import com.example.weeking.databinding.ActivityNuevaActividadBinding;
import com.example.weeking.databinding.ActivityNuevoEventoBinding;
import com.example.weeking.entity.EventoClass;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NuevoEventoActivity extends AppCompatActivity {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int MAP_REQUEST_CODE = 1;

    private LatLng ubicacionSeleccionada;
    private String nombreUbicacion;

    private  double latitud;
    private  double longitud;


    ActivityNuevoEventoBinding binding;
    String  idActividad;
    String idEvento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNuevoEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idActividad = getIntent().getStringExtra("id_actividad");
        idEvento = getIntent().getStringExtra("id_evento");

        // Log para verificar los IDs obtenidos de la actividad anterior
        Log.d("idActividad:Activity", idActividad != null ? idActividad : "null or not found");
        Log.d("idEvento:Activity", idEvento != null ? idEvento : "null or not found");

        binding.saveEventButton.setOnClickListener(v -> {
            String nombre = binding.nombreEvento.getText().toString();
            String descripcion = binding.descriptionEditText.getText().toString();
            String fecha = binding.tvSelectedDate.getText().toString();
            String hora = binding.tvSelectedTime.getText().toString();

            // Llamar a la función para crear o actualizar el evento
            crearOActualizarEvento(nombre, descripcion, fecha, hora);
            // Navegar de regreso a la Actividad principal o donde sea necesario
            finish(); // Finalizar esta actividad si no necesitas volver a ella
        });

        // Otros listeners y configuraciones...
        // ...

        // Cargar datos del evento si el idEvento no es nulo y no está vacío
        if (idEvento != null && !idEvento.isEmpty()) {
            cargarDatosEvento(idEvento);
        }

        // Mostrar dialogo de fecha y hora cuando se hacen clic en los iconos respectivos
        binding.iconSelectDate.setOnClickListener(v -> mostrarDialogoFecha());
        binding.iconSelectTime.setOnClickListener(v -> mostrarDialogoHora());
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
                        // ... más asignaciones de campos
                        // También debes convertir la fecha y la hora del timestamp a String y mostrarlo en los TextViews correspondientes
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                        binding.tvSelectedDate.setText(sdf.format(evento.getFecha_evento().toDate()));
                        // Para la hora, posiblemente necesites ajustar el formato para que coincida con cómo se muestra en la UI
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

        // Preparar el objeto evento con los datos recibidos
        EventoClass evento = new EventoClass();
        evento.setNombre(nombre);
        evento.setDescripcion(descripcion);
        evento.setEstado(true);
        evento.setUbicacion(nombreUbicacion);
        evento.setLatitud(latitud);
        evento.setLongitud(longitud);
        evento.setIdActividad(idActividad);
        evento.setFoto("url_a_la_foto");
        evento.setLikes(0);
        evento.setListaUsuariosIds(new ArrayList<>());

        // Convertir la fecha y hora en un objeto Timestamp para Firestore
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            Date fechaHora = sdf.parse(fecha + " " + hora);
            evento.setFechaEvento(new Timestamp(fechaHora));
        } catch (ParseException e) {
            e.printStackTrace();
            evento.setFechaEvento(new Timestamp(new Date()));
        }

        DocumentReference eventoRef;

        // Verificar si el idEvento ya existe para actualizar el evento existente
        if (idEvento != null && !idEvento.isEmpty()) {
            eventoRef = db.collection("Eventos").document(idEvento);
            evento.setEventId(idEvento); // Asegúrate de establecer el ID del evento si lo estás actualizando
        } else {
            eventoRef = db.collection("Eventos").document(); // Crea un nuevo documento con un ID único
            // No necesitas establecer el ID del evento aquí porque se generará uno nuevo
        }

        // Agregar o actualizar el evento en Firestore
        eventoRef.set(evento)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Evento guardado con éxito con ID: " + eventoRef.getId());

                    // Si es un evento nuevo, actualiza la lista de eventos en 'actividad'
                    if (idEvento == null || idEvento.isEmpty()) {
                        CollectionReference actividadRef = db.collection("actividad");
                        actividadRef.document(idActividad)
                                .update("listaEventosIds", FieldValue.arrayUnion(eventoRef.getId()));
                    }

                    Toast.makeText(NuevoEventoActivity.this, "Evento guardado con éxito!", Toast.LENGTH_SHORT).show();

                    // Finalizar la actividad o actualizar la interfaz de usuario según sea necesario
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error al guardar el evento", e);
                    Toast.makeText(NuevoEventoActivity.this, "Error al guardar el evento.", Toast.LENGTH_SHORT).show();
                });
    }


}