package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.weeking.entity.ListaDon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import com.example.weeking.R;

import java.util.HashMap;

public class Pago extends AppCompatActivity {

    FirebaseFirestore db;
    StorageReference storageReference;
    String storage_path = "donacion/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd;
    ImageView foto;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);
        db = FirebaseFirestore.getInstance();
        String codigoactual = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("idauth",codigoactual);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        foto = findViewById(R.id.image);
        Button comprobante = findViewById(R.id.button11);
        foto.setOnClickListener(v -> {
                uploadPhoto();
        });
        comprobante.setOnClickListener(v -> {
            Log.d("idauth",mAuth.getUid());
        });
    }

    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }
    private void subirPhoto(Uri image_url) {
        progressDialog.setMessage("Actualizando foto");
        progressDialog.show();
        String rute_storage_photo = storage_path + "" + photo + "" + mAuth.getUid() +""+ idd;
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
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("foto", download_uri);
                            db.collection("donaciones").document(idd).update(map);
                            Toast.makeText(Pago.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Pago.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private ListaDon getusuario(String id){
        ListaDon dona = null;
        db.collection("donaciones").document(id).get().addOnSuccessListener(documentSnapshot -> {
            dona.setCodigo(documentSnapshot.getString("codigo"));
            dona.setFoto(documentSnapshot.getString("foto"));
            dona.setNombre(documentSnapshot.getString("nombre"));
            dona.setEgresado(documentSnapshot.getBoolean("egresado"));
            dona.setMonto(Integer.valueOf(documentSnapshot.getString("monto")));
            dona.setRechazo(documentSnapshot.getString("rechazo"));
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
        return dona;
    }


}