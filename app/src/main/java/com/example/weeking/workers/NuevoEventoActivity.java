package com.example.weeking.workers;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.weeking.R;
import com.example.weeking.databinding.ActivityNuevaActividadBinding;
import com.example.weeking.databinding.ActivityNuevoEventoBinding;
import com.example.weeking.entity.EventoClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NuevoEventoActivity extends AppCompatActivity {

    ActivityNuevoEventoBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNuevoEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.saveEventButton.setOnClickListener(v -> {
            String nombre = binding.nombreEvento.getText().toString();
            String descripcion = binding.descriptionEditText.getText().toString();
            String lugar_evento = binding.lugarEvento.getText().toString();
            String fecha = binding.tvSelectedDate.getText().toString();
            String hora = binding.tvSelectedTime.getText().toString();


            Log.d("mensajes_retorno",fecha+hora);
            crearNuevoEvento(nombre, descripcion,fecha,hora,lugar_evento);

        });

        binding.iconSelectDate.setOnClickListener(v -> mostrarDialogoFecha());
        binding.iconSelectTime.setOnClickListener(v -> mostrarDialogoHora());
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




    private void crearNuevoEvento(String nombre, String descripcion, String fecha, String hora , String ubicacion) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventosRef = db.collection("Eventos");

        // Crear un nuevo objeto de evento
        EventoClass evento = new EventoClass();
        evento.setNombre(nombre);
        evento.setDescripcion(descripcion);
        evento.setEstado(true);

        // Convertir la fecha y hora en un objeto Timestamp para Firestore
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date fechaHora = sdf.parse(fecha + " " + hora);
            evento.setFechaEvento(new Timestamp(fechaHora));
        } catch (ParseException e) {
            e.printStackTrace();
            // Manejar el error o establecer una fecha/hora predeterminada si es necesario
            evento.setFechaEvento(new Timestamp(new Date()));
        }

        evento.setFoto("https://upload.wikimedia.org/wikipedia/commons/thumb/9/9c/Chess_board_with_chess_set_in_opening_position_2012_PD_03.jpg/1024px-Chess_board_with_chess_set_in_opening_position_2012_PD_03.jpg");
        evento.setLikes(0);
        evento.setUbicacion(ubicacion);
        evento.setListaUsuariosIds(new ArrayList<>());

        // Agregar el evento a Firestore
        eventosRef.add(evento)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Documento añadido con ID: " + documentReference.getId());

                    // Actualizar el ID en el objeto evento y en Firestore
                    evento.setEventId(documentReference.getId());
                    eventosRef.document(documentReference.getId()).set(evento);
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error añadiendo el documento", e));
    }

}