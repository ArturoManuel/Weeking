package com.example.weeking.Adapter;

import android.app.AlertDialog;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weeking.R;
import com.example.weeking.entity.Alumno;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;

public class AlumnoAdapter extends RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder> {

    private Context context;
    private List<Alumno> listaAlumnos;
    private String idactividad;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String codigoAlumnoSeleccionado; // Código del alumno seleccionado

    public AlumnoAdapter(Context context, List<Alumno> listaAlumnos, String idactividad) {
        this.context = context;
        this.listaAlumnos = listaAlumnos;
        this.idactividad = idactividad;
    }


    public void setCodigoAlumnoSeleccionado(String codigo) {
        codigoAlumnoSeleccionado = codigo;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.elemento_alumno, parent, false);
        return new AlumnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoViewHolder holder, int position) {
        Alumno alumnoActual = listaAlumnos.get(position);
        holder.textViewAlumno.setText(alumnoActual.getNombre());

        // Establecer el estado del botón basado en si el alumno está seleccionado
        boolean estaAñadido = alumnoActual.getCodigo().equals(codigoAlumnoSeleccionado);
        actualizarEstadoBoton(holder, estaAñadido);

        holder.buttonAnadir.setOnClickListener(v -> {
            // Cambiar la selección y actualizar la base de datos
            if (estaAñadido) {
                // Mostrar diálogo de confirmación
                mostrarDialogoConfirmacion(context, alumnoActual);
            } else {
                // Cambiar la selección y actualizar la base de datos
                actualizarUsuarioActividad(codigoAlumnoSeleccionado, false); // Desmarcar el anterior
                actualizarUsuarioActividad(alumnoActual.getCodigo(), true); // Marcar el nuevo
                codigoAlumnoSeleccionado = alumnoActual.getCodigo(); // Actualizar el código del alumno seleccionado
                notifyDataSetChanged(); // Actualizar toda la lista para reflejar los cambios
            }
        });
    }

    private void actualizarEstadoBoton(AlumnoViewHolder holder, boolean estaAñadido) {
        holder.buttonAnadir.setText(estaAñadido ? "Añadido" : "Añadir");
        holder.buttonAnadir.setBackgroundColor(estaAñadido ? Color.BLACK : Color.GRAY);
    }

    private void actualizarUsuarioActividad(String codigoAlumno, boolean anadir) {
        if (codigoAlumno == null) return;

        if (anadir) {
            // Añadir el idActividad al alumno seleccionado y cambiar su rol a "delegado_de_actividad"
            db.collection("usuarios").document(codigoAlumno)
                    .update("activity", FieldValue.arrayUnion(idactividad),
                            "rol", "delegado_de_actividad")
                    .addOnSuccessListener(aVoid -> Log.d("AlumnoAdapter", "Actividad y rol actualizados con éxito."))
                    .addOnFailureListener(e -> Log.w("AlumnoAdapter", "Error al actualizar actividad y rol", e));
        } else {
            // Eliminar el idActividad del alumno deseleccionado y cambiar su rol a "alumno"
            db.collection("usuarios").document(codigoAlumno)
                    .update("activity", FieldValue.arrayRemove(idactividad),
                            "rol", "alumno")
                    .addOnSuccessListener(aVoid -> Log.d("AlumnoAdapter", "Actividad eliminada y rol actualizado con éxito."))
                    .addOnFailureListener(e -> Log.w("AlumnoAdapter", "Error al eliminar actividad y actualizar rol", e));
        }
    }



    private void mostrarDialogoConfirmacion(Context context, Alumno alumno) {
        new AlertDialog.Builder(context)
                .setTitle("Confirmación")
                .setMessage("¿Seguro que quieres eliminar a este delegado de actividad?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    // Eliminar el delegado de actividad
                    actualizarUsuarioActividad(alumno.getCodigo(), false);
                    codigoAlumnoSeleccionado = null;
                    notifyDataSetChanged();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


    @Override
    public int getItemCount() {
        return listaAlumnos.size();
    }


    static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAlumno;
        Button buttonAnadir;

        AlumnoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAlumno = itemView.findViewById(R.id.alumno);
            buttonAnadir = itemView.findViewById(R.id.anadir);
        }
    }
}

