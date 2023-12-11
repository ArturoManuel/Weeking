package com.example.weeking.Adapter;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static java.time.temporal.ChronoUnit.DAYS;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.Usuario;
import com.example.weeking.workers.NuevoEventoActivity;
import com.example.weeking.workers.VistaEventoActivity;
import com.example.weeking.workers.VistaPrincipal;
import com.example.weeking.workers.viewModels.AppViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdaptadorPrin extends RecyclerView.Adapter<AdaptadorPrin.ViewHolder> {
    private List<EventoClass> mdata;
    private LayoutInflater minflater;
    private Context context;
    private AppViewModel appViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
            FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

    public AdaptadorPrin(List<EventoClass> itemlist, Context context) {
        this.minflater = LayoutInflater.from(context);
        this.context = context;
        this.mdata = itemlist;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = minflater.inflate(R.layout.lista_principal, parent, false); // Añadido parent y false
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return (mdata != null) ? mdata.size() : 0;  // Protección contra null
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.bindData(mdata.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView evento;
        ImageView foto;
        TextView fecha;
        TextView ubicacion;
        TextView estado;

        TextView descripcion, fotito;
        ImageView likeButton;
        EventoClass eventoSeleccionado;
        ViewHolder(View itemView) {
            super(itemView);
            evento = itemView.findViewById(R.id.nombreEvento);
            fecha= itemView.findViewById(R.id.fecha);
            foto = itemView.findViewById(R.id.foto);
            ubicacion= itemView.findViewById(R.id.ubicacion);
            descripcion= itemView.findViewById(R.id.descripcion);
            fotito = itemView.findViewById(R.id.fotito);
            estado = itemView.findViewById(R.id.txtViewEstado);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                eventoSeleccionado = mdata.get(position);
                DataHolder.getInstance().setEventoSeleccionado(eventoSeleccionado);
                Intent intent = new Intent(context, VistaEventoActivity.class);
                intent.putExtra("evento", (String) evento.getText());
                intent.putExtra("descripcion", (String) descripcion.getText());
                intent.putExtra("ubicacion", (String) ubicacion.getText());
                intent.putExtra("foto", (String) fotito.getText());
                intent.putExtra("estado", (String) estado.getText());
                context.startActivity(intent);
            });
//            likeButton = itemView.findViewById(R.id.like_btn);
//            likeButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //tiene que existir la logica para que contabilice los likes por cada evento
//
//                }
//            });


        }

        void bindData(final EventoClass item) {
            evento.setText(item.getNombre());
            descripcion.setText(item.getDescripcion());
            Date date = item.getFecha_evento().toDate();
            // Formatear la fecha
            LocalDate localDate = LocalDate.now();
            //Log.d("MSG", String.valueOf(localDate));
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d HH:mm", Locale.getDefault());
            String formattedDate = sdf.format(date);
            // Capitalizar la primera letra de cada palabra
            formattedDate = capitalize(formattedDate);
            fecha.setText(formattedDate);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateOp = format.format(date);
            //Log.d("MSG", dateOp);
            long numberOFDays = DAYS.between(LocalDate.parse(dateOp), localDate);
            //Log.d("msg", String.valueOf(numberOFDays));
            /*if(numberOFDays > 0){
                HashMap<String, Object> map = new HashMap<>();
                Boolean noDisponible = false;
                map.put("estado", noDisponible);
                db.collection("Eventos").document(item.getEventId()).update(map);
            }*/
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
                                estado.setVisibility(View.INVISIBLE);
                            } else if ("delegado_de_actividad".equals(rol)) {
                                estado.setVisibility(View.VISIBLE);
                            } else if ("alumno".equals(rol)) {
                                estado.setVisibility(View.INVISIBLE);
                            } else {
                                Log.d(TAG, "Rol no reconocido");
                                return; // Sale de la función.
                            }

                            Boolean dispo = item.isEstado();
                            if (dispo == true || numberOFDays <= 0){
                                estado.setText("En proceso");
                            } else {
                                estado.setText("Terminado");
                            }

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });
            }

            ubicacion.setText(item.getUbicacion());
            fotito.setText(item.getFoto());
            String imageUrl = item.getFoto();  // Asume que `item.getImageUrl()` proporciona la URL de la imagen
            Glide.with(foto.getContext())
                    .load(imageUrl)
                    .into(foto);  // `foto` es tu ImageView
        }

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
    public void updateData(List<EventoClass> newData) {
        this.mdata.clear();
        this.mdata.addAll(newData);
        notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
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
                        AppViewModel appViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(AppViewModel.class);
                        appViewModel.setCurrentUser(usuario);
                    }
                })
                .addOnFailureListener(e -> {
                });
    }
}
