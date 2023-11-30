package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.Adapter.AdaptadorAlu;
import com.example.weeking.Adapter.AdapterMensajes;
import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.ListaDon;
import com.example.weeking.entity.Mensaje;
import com.example.weeking.entity.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    private CircleImageView fotoPerfil;
    private TextView nombre;
    private EditText txtMensaje;
    private Button btnEnviar;
    private ImageButton btnEnviarFoto;

    StorageReference storageReference;
    String storage_path = "chat/*";

    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;

    String download_uri = "";
    ProgressDialog progressDialog;
    List<Mensaje> mensaje2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        fotoPerfil = findViewById(R.id.fotoPerfil);
        nombre = findViewById(R.id.nombre);
        txtMensaje = findViewById(R.id.txtMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnEnviarFoto = findViewById(R.id.btnEnviarFoto);
        final String[] nombre1 = {null};
        final String[] foto = {null};
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EventoClass eventoSeleccionado = DataHolder.getInstance().getEventoSeleccionado();

        Picasso.get().load(eventoSeleccionado.getFoto()).into(fotoPerfil);
        nombre.setText(eventoSeleccionado.getNombre());
        db.collection("chat1").orderBy("fecha", Query.Direction.ASCENDING).addSnapshotListener((collection, error) -> {
            if (error != null) {
                Log.d("lectura", "Error listening for document changes.");
                return;
            }
            if (collection != null && !collection.isEmpty()) {
                mensaje2.clear();
                for (QueryDocumentSnapshot document1 : collection) {
                    try {
                        Mensaje mens = document1.toObject(Mensaje.class);
                        if(mens.getType_mensaje().equals(eventoSeleccionado.getEventId())){
                            mensaje2.add(mens);
                        }
                    } catch (Exception e) {
                        Log.e("asdf", "Error processing document", e);
                    }
                }
                AdapterMensajes listAdapter = new AdapterMensajes(mensaje2,this);
                RecyclerView recyclerView = findViewById(R.id.rvMensajes);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
            }
        });

        Log.d("asdfg",FirebaseAuth.getInstance().getCurrentUser().getUid());
        Query query = db.collection("usuarios").whereEqualTo("authUID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.get().addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                QuerySnapshot queryDocumentSnapshot = task.getResult();
                if(!queryDocumentSnapshot.isEmpty()){
                    DocumentSnapshot document = queryDocumentSnapshot.getDocuments().get(0);
                    Log.d("asdfg",document.getString("imagen_url"));
                    nombre1[0] = document.getString("nombre");
                    foto[0] = document.getString("imagen_url");
                }
            }});

        btnEnviar.setOnClickListener(view ->  {
            if(!txtMensaje.getText().toString().trim().equals("")){
                HashMap<String, Object> map = new HashMap<>();
                map.put("mensaje",txtMensaje.getText().toString());
                map.put("nombre", nombre1[0]);
                map.put("fotoPerfil", foto[0]);
                map.put("type_mensaje",eventoSeleccionado.getEventId());
                map.put("urlFoto","");
                map.put("fecha", Instant.now().toEpochMilli());
                db.collection("chat1").document().set(map);
                txtMensaje.setText("");
        }

        });

        btnEnviarFoto.setOnClickListener(view -> {
            uploadPhoto();
        });

    }

    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
        Log.d("zxc","a");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                Log.d("zxc","b");
                subirPhoto(image_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPhoto(Uri image_url) {
        EventoClass eventoSeleccionado = DataHolder.getInstance().getEventoSeleccionado();
        progressDialog.setMessage("Subiendo foto");
        progressDialog.show();
        Log.d("zxc","d");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Log.d("zxc","e");

        String uniqueId = UUID.randomUUID().toString(); // Generador de ID único
        String rute_storage_photo = storage_path + auth.getCurrentUser().getUid() + "/" + uniqueId;

        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if (uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            download_uri = uri.toString();
                            Log.d("zxc","f");
                            Query query = db.collection("usuarios").whereEqualTo("authUID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            query.get().addOnCompleteListener(task ->{
                                if(task.isSuccessful()){
                                    Log.d("zxc","g");
                                    QuerySnapshot queryDocumentSnapshot = task.getResult();
                                    if(!queryDocumentSnapshot.isEmpty()){
                                        DocumentSnapshot document = queryDocumentSnapshot.getDocuments().get(0);
                                        String nombre = document.getString("nombre");
                                        String foto = document.getString("imagen_url");
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("mensaje","");
                                        map.put("nombre", nombre);
                                        map.put("fotoPerfil", foto);
                                        map.put("type_mensaje",eventoSeleccionado.getEventId());
                                        map.put("urlFoto",download_uri);
                                        map.put("fecha", Instant.now().toEpochMilli());
                                        db.collection("chat1").document().set(map);
                                        Log.d("zxc","h");
                                    }
                                }
                            } );
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Chat.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

}