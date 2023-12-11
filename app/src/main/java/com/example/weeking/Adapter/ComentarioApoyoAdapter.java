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
import com.example.weeking.entity.Usuario;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
                apoyo = usuario.getApoyo();
            }

            tvTipoApoyo.setText("Apoyo: "+apoyo);
        }
    }

    private void mostrarDialogoDeAccion(Usuario usuario, int position) {
        verificarCantidadDeBarra(eventoId, permitido -> {
            limiteAlcanzadoBarra = !permitido;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirmar Acción");
            builder.setMessage("Tipo de apoyo: " + usuario.getComentariosDeApoyo().get(eventoId).getTipoApoyo() + "\nComentario: " + usuario.getComentariosDeApoyo().get(eventoId).getComentario());

            // Agregar botón "Aceptar" con verificación
            builder.setPositiveButton("Aceptar", (dialog, id) -> {
                if (limiteAlcanzadoBarra && usuario.getComentariosDeApoyo().get(eventoId).getTipoApoyo().equals("Barra")) {
                    Toast.makeText(context, "Ya se ha alcanzado el límite de 'Barra' para este evento", Toast.LENGTH_SHORT).show();
                } else {
                    actualizarEstadoDeApoyo(usuario, usuario.getComentariosDeApoyo().get(eventoId).getTipoApoyo(), position);
                }
            });

            // Agregar botón "Denegar"
            builder.setNegativeButton("Denegar", (dialog, id) -> actualizarEstadoDeApoyo(usuario, "denegado", position));

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void actualizarEstadoDeApoyo(Usuario usuario, String nuevoEstado, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("usuarios").document(usuario.getCodigo());

        // Crear un mapa con el nuevo estado de apoyo
        Map<String, Object> update = new HashMap<>();
        update.put("apoyo", nuevoEstado);

        // Actualizar el documento de Firestore
        userRef.update(update)
                .addOnSuccessListener(aVoid -> {
                    // Actualiza el estado de apoyo en el objeto Usuario y notifica al adapter
                    usuario.setApoyo(nuevoEstado);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Estado de apoyo actualizado a: " + nuevoEstado, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al actualizar el estado de apoyo", Toast.LENGTH_SHORT).show();
                });
    }




    private void verificarCantidadDeBarra(String eventoId, VerificacionCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .whereEqualTo("apoyo", "Barra")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int contador = 0;
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Usuario usuario = document.toObject(Usuario.class);
                        if (usuario.getComentariosDeApoyo().containsKey(eventoId)) {
                            contador++;
                        }
                    }
                    callback.onVerificado(contador < 10);
                })
                .addOnFailureListener(e -> {
                    Log.e("ComentarioApoyoAdapter", "Error al verificar la cantidad de 'Barra'", e);
                    callback.onVerificado(false);
                });
    }


}
