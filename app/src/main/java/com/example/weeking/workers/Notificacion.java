package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.example.weeking.Adapter.AdaptarNoti;
import com.example.weeking.R;
import com.example.weeking.entity.Noti;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Notificacion extends AppCompatActivity {
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        List<Noti> notif = new ArrayList<>();
        RecyclerView noti = findViewById(R.id.noti);
        db = FirebaseFirestore.getInstance();
        AdaptarNoti listaAdapter = new AdaptarNoti(notif,this);
        Query query = db.collection("usuarios").whereEqualTo("authUID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.get().addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                QuerySnapshot queryDocumentSnapshot = task.getResult();
                if(!queryDocumentSnapshot.isEmpty()){
                    DocumentSnapshot document = queryDocumentSnapshot.getDocuments().get(0);
                    Log.d("asdfg",document.getString("codigo"));
                    Query query1 = db.collection("noti").whereEqualTo("codigo", document.getString("codigo"));
                    query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@android.support.annotation.Nullable QuerySnapshot snapshots,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("asdfg", "Escucha fallida.", e);
                                return;
                            }
                            if (snapshots != null && !snapshots.isEmpty()) {
                                Log.d("asdfg", "Cambios detectados en la colección");
                                try {
                                    notif.clear();
                                    for (DocumentSnapshot document : snapshots.getDocuments()) {
                                        Noti no = document.toObject(Noti.class);
                                        notif.add(no);
                                    }
                                } catch (Exception ex) {
                                    Log.d("asdfg", "Se produjo un error: " + ex.getMessage());
                                }
                                listaAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }}
        });
        RecyclerView recyclerView = findViewById(R.id.noti);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listaAdapter);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Notificacion");
        }
    }
}