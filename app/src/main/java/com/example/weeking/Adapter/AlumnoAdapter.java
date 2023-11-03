package com.example.weeking.Adapter;

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
    private List<Alumno> listaAlumnos;  // Asumo que tienes una clase Alumno definida previamente

    private  String idactividad;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public AlumnoAdapter(Context context, List<Alumno> listaAlumnos , String idactividad) {
        this.context = context;
        this.listaAlumnos = listaAlumnos;
        this.idactividad=idactividad;
    }

    @NonNull
    @Override
    public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.elemento_alumno, parent, false);  // Asume que tu archivo XML se llama "elemento_alumno.xml"
        return new AlumnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoViewHolder holder, int position) {
        Alumno alumnoActual = listaAlumnos.get(position);
        holder.textViewAlumno.setText(alumnoActual.getNombre());

        holder.buttonAnadir.setOnClickListener(v -> {
            // Obtener el alumno actual basado en la posición
            // Suponiendo que tienes un campo identificador único para cada alumno, como un 'id'
            String alumnoId = alumnoActual.getCodigo();

            if(alumnoId != null) {
                // Actualizar el rol del alumno en Firestore
                db.collection("usuarios").document(alumnoId)
                        .update("rol", "delegado_de_actividad")
                        .addOnSuccessListener(aVoid -> Log.d("AlumnoAdapter", "Rol actualizado con éxito."))
                        .addOnFailureListener(e -> Log.w("AlumnoAdapter", "Error actualizando el rol", e));

                // Crear un nuevo documento en la colección UsuarioActividad
                Map<String, Object> usuarioActividad = new HashMap<>();
                usuarioActividad.put("usuarioId", alumnoId);
                usuarioActividad.put("idActividad", idactividad);

                db.collection("UsuarioActividad")
                        .add(usuarioActividad)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("AlumnoAdapter", "UsuarioActividad añadido con ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("AlumnoAdapter", "Error añadiendo UsuarioActividad", e);
                            }
                        });


                // Agregar el idActividad a una lista en el documento del alumno
                // Suponemos que tienes un campo en el documento llamado 'actividades' que es un tipo Array
                db.collection("usuarios").document(alumnoId)
                        .update("activity", FieldValue.arrayUnion(idactividad))
                        .addOnSuccessListener(aVoid -> Log.d("AlumnoAdapter", "ID de actividad añadido con éxito."))
                        .addOnFailureListener(e -> Log.w("AlumnoAdapter", "Error añadiendo ID de actividad", e));
            } else {
                Log.e("AlumnoAdapter", "alumnoId es null");
            }
            });






    }

    @Override
    public int getItemCount() {
        return listaAlumnos.size();
    }

    class AlumnoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAlumno;
        Button buttonAnadir;

        public AlumnoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAlumno = itemView.findViewById(R.id.alumno);
            buttonAnadir = itemView.findViewById(R.id.anadir);
        }
    }
}
