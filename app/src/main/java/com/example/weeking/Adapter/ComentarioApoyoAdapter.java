package com.example.weeking.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weeking.R;
import com.example.weeking.entity.ComentarioDeApoyo;
import com.example.weeking.entity.EventoApoyo;
import com.example.weeking.entity.Usuario;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComentarioApoyoAdapter extends RecyclerView.Adapter<ComentarioApoyoAdapter.ComentarioApoyoViewHolder> {

    private List<Usuario> comentariosApoyoList;
    private LayoutInflater mInflater;

    private String eventoId;
    private Context context;
    private boolean limiteAlcanzadoBarra = false;


    interface VerificacionCallback {
        void onVerificado(boolean permitido);
    }

    public ComentarioApoyoAdapter(Context context, List<Usuario> comentariosApoyoList , String eventoId) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.comentariosApoyoList = comentariosApoyoList;
        this.eventoId = eventoId;
    }

    @NonNull
    @Override
    public ComentarioApoyoAdapter.ComentarioApoyoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_comentario_apoyo, parent, false);
        return new ComentarioApoyoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioApoyoAdapter.ComentarioApoyoViewHolder holder, int position) {
        Usuario usuario = comentariosApoyoList.get(position);
        holder.bindData(usuario);
    }

    @Override
    public int getItemCount() {
        return comentariosApoyoList != null ? comentariosApoyoList.size() : 0;
    }

    public class ComentarioApoyoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre;
        TextView tvCodigo;
        TextView tvTipoApoyo;

        public ComentarioApoyoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvTipoApoyo = itemView.findViewById(R.id.tvTipoApoyo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtén la posición del ítem presionado
                    int position = getAdapterPosition();
                    // Asegúrate de que la posición es válida
                    if (position != RecyclerView.NO_POSITION) {
                        Usuario usuario = comentariosApoyoList.get(position);
                        mostrarDialogoDeAccion(usuario, position);
                    }
                }
            });
        }

        void bindData(Usuario usuario) {
            // Aquí asignas los valores de los atributos de Usuario a las vistas
            tvNombre.setText("Nombre:"+usuario.getNombre());
            tvCodigo.setText("Código:"+usuario.getCodigo());
            String apoyo ="";
            if(usuario.getApoyo().equals("en_proceso")){
                apoyo = "En proceso de aprobar";
            }else if(usuario.getApoyo().equals("denegado")){
                apoyo = "Denegado";
            }else {
                if(usuario.getApoyoList().get(eventoId)!=null){
                    apoyo = usuario.getApoyoList().get(eventoId);
                }else{
                    apoyo="nulo";
                }

            }

            tvTipoApoyo.setText("Apoyo: "+apoyo);
        }
    }

    private void mostrarDialogoDeAccion(Usuario usuario, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirmar Acción");
            builder.setMessage("Tipo de apoyo: " + usuario.getComentariosDeApoyo().get(eventoId).getTipoApoyo() + "\nComentario: " + usuario.getComentariosDeApoyo().get(eventoId).getComentario());
        builder.setPositiveButton("Aceptar", (dialog, id) -> {
            actualizarApoyoListEnFirebase(usuario, "apoya", eventoId, () -> actualizarEstadoApoyoEnFirebase(usuario, position));
        });
        builder.setNegativeButton("Denegar", (dialog, id) -> {
            actualizarApoyoListEnFirebase(usuario, "denegado", eventoId, () -> actualizarEstadoApoyoEnFirebase(usuario, position));
        });
            AlertDialog dialog = builder.create();
            dialog.show();
    }

//    private void actualizarEstadoDeApoyo(Usuario usuario, String nuevoEstado, int position, String eventoId) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference userRef = db.collection("usuarios").document(usuario.getCodigo());
//        Map<String, String> apoyoList = usuario.getApoyoList();
//        if (apoyoList == null) {
//            apoyoList = new HashMap<>();
//        }
//        if(nuevoEstado.equals("denegado")){
//            apoyoList.put(eventoId,nuevoEstado);
//        } else if (nuevoEstado.equals("apoya")) {
//            apoyoList.put(eventoId,usuario.getComentariosDeApoyo().get(eventoId).getTipoApoyo());
//        }
//
//
//        String estadoGeneralApoyo = "en_proceso";
//        // Determinar el estado general de apoyo
//        for (String estado : apoyoList.values()) {
//            if (!"denegado".equals(estado)) {
//                estadoGeneralApoyo = "apoya";
//                break;
//            }
//        }
//
//        // Si todos los eventos están denegados, entonces el estado general es "denegado"
//        if (apoyoList.values().stream().allMatch("denegado"::equals)) {
//            estadoGeneralApoyo = "denegado";
//        }
//        // Preparar la actualización para Firestore
//        final Map<String, String> finalApoyoList = new HashMap<>(apoyoList);
//        final String finalEstadoGeneralApoyo = estadoGeneralApoyo;
//        Map<String, Object> update = new HashMap<>();
//        update.put("apoyoList", finalApoyoList);
//        update.put("apoyo", estadoGeneralApoyo);
//
//        // Realizar la actualización en Firestore
//        userRef.update(update)
//                .addOnSuccessListener(aVoid -> {
//                    usuario.setApoyoList(finalApoyoList);
//                    usuario.setApoyo(finalEstadoGeneralApoyo);
//                    notifyItemChanged(position);
//                    Toast.makeText(context, "Estado de apoyo actualizado a: " + finalEstadoGeneralApoyo, Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(context, "Error al actualizar el estado de apoyo", Toast.LENGTH_SHORT).show();
//                });
//    }



    private void actualizarApoyoListEnFirebase(Usuario usuario, String nuevoEstado, String eventoId, Runnable onSuccessCallback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("usuarios").document(usuario.getCodigo());

        Map<String, String> apoyoList = usuario.getApoyoList();
        if (apoyoList == null) {
            apoyoList = new HashMap<>();
        }
        if (nuevoEstado.equals("apoya")){
            apoyoList.put(eventoId, usuario.getComentariosDeApoyo().get(eventoId).getTipoApoyo());
        }else {
            apoyoList.put(eventoId, nuevoEstado);
        }

        final Map<String, String> finalApoyoList = new HashMap<>(apoyoList);
        userRef.update("apoyoList", apoyoList)
                .addOnSuccessListener(aVoid -> {
                    usuario.setApoyoList(finalApoyoList);
                    if (onSuccessCallback != null) {
                        onSuccessCallback.run(); // Ejecutar el callback una vez que apoyoList se ha actualizado con éxito
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Error al actualizar la lista de apoyo", Toast.LENGTH_SHORT).show()
                );
    }

    private void actualizarEstadoApoyoEnFirebase(Usuario usuario, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("usuarios").document(usuario.getCodigo());

        // Determinar el estado general de apoyo basado en apoyoList
        String estadoGeneralApoyo = determinarEstadoGeneralApoyo(usuario.getApoyoList());

        userRef.update("apoyo", estadoGeneralApoyo)
                .addOnSuccessListener(aVoid -> {
                    usuario.setApoyo(estadoGeneralApoyo); // Actualiza el objeto usuario
                    notifyItemChanged(position); // Notifica al adaptador del cambio para actualizar la UI
                    Toast.makeText(context, "Se actualizo el apoyo exitosamente" + estadoGeneralApoyo, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Error al actualizar el estado de apoyo", Toast.LENGTH_SHORT).show()
                );
    }

    private String determinarEstadoGeneralApoyo(Map<String, String> apoyoList) {
        for (String estado : apoyoList.values()) {
            if (!"denegado".equals(estado)) {
                return "apoya";
            }
        }
        return "denegado";
    }





}
