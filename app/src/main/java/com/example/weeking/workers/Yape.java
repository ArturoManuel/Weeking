package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class Yape extends AppCompatActivity {
    FirebaseFirestore db;
    StorageReference storageReference;
    String storage_path = "usuarios/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd;
    ImageView foto;

    String download_uri = "";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yape);

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();

        Button siguiente = findViewById(R.id.button9);
        siguiente.setOnClickListener(v -> {
            uploadPhoto();

        });
    }
    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                subirPhoto(image_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPhoto(Uri image_url) {
        progressDialog.setMessage("Subiendo foto");
        progressDialog.show();
        String rute_storage_photo = storage_path +""+ FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("abssd", rute_storage_photo);
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
                            Query query = db.collection("usuarios").whereEqualTo("authUID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            query.get().addOnCompleteListener(task ->{
                                if(task.isSuccessful()){
                                    QuerySnapshot queryDocumentSnapshot = task.getResult();
                                    if(!queryDocumentSnapshot.isEmpty()){
                                        DocumentSnapshot document = queryDocumentSnapshot.getDocuments().get(0);
                                        String codigo = document.getString("codigo");
                                        Query query1 = db.collection("donaciones").whereEqualTo("codigo",codigo);
                                        query1.get().addOnCompleteListener(task1 ->{
                                            if(task.isSuccessful()){
                                                Log.d("nombre",codigo);
                                                QuerySnapshot queryDocumentSnapshot1 = task1.getResult();
                                                if(!queryDocumentSnapshot1.isEmpty()){
                                                    DocumentSnapshot document1 = queryDocumentSnapshot1.getDocuments().get(0);
                                                    HashMap<String, Object> map = new HashMap<>();
                                                    map.put("foto",download_uri);
                                                    map.put("rechazo","1");
                                                    db.collection("donaciones").document(document.getString("codigo")).update(map);
                                                    Intent intent = new Intent(Yape.this, VistaPrincipal.class);
                                                    startActivity(intent);
                                                }else{
                                                    HashMap<String, Object> map = new HashMap<>();
                                                    map.put("foto", download_uri);
                                                    map.put("nombre",document.getString("nombre"));
                                                    map.put("codigo",document.getString("codigo"));
                                                    map.put("monto",0);
                                                    map.put("rechazo","1");
                                                    map.put("egresado",true);
                                                    db.collection("donaciones").document(document.getString("codigo")).set(map);
                                                    Intent intent = new Intent(Yape.this, VistaPrincipal.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        } );
                                    }
                                }
                            } );
                            Toast.makeText(Yape.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Yape.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void subirPhotoNueva(Uri image_url) {
        progressDialog.setMessage("Subiendo foto");
        progressDialog.show();

        // Obtener el ID único del usuario actual.
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Generar un nombre de archivo único para la imagen.
        String uniqueFileName = UUID.randomUUID().toString() + ".jpg"; // Añade .jpg si quieres que el archivo sea un JPEG.

        // Crear la ruta completa donde se guardará la imagen, e.g., "usuarios/userId/uniqueFileName"
        String storagePath = "usuarios/" + userId + "/" + uniqueFileName;

        StorageReference reference = storageReference.child(storagePath);
        reference.putFile(image_url)
                .addOnSuccessListener(taskSnapshot -> {
                    // Obtiene la URL de descarga
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        download_uri = uri.toString();
                        // Aquí actualizas la base de datos con la nueva URL de la foto
                        // ...

                        Toast.makeText(Yape.this, "Foto subida con éxito", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        // Sigue con el flujo de tu aplicación, por ejemplo, lanzar otra actividad
                        startActivity(new Intent(Yape.this, VistaPrincipal.class));
                    });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(Yape.this, "Error al subir la foto: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

}