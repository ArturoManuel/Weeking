package com.example.weeking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weeking.R;
import com.example.weeking.dataHolder.DataHolder;
import com.example.weeking.entity.Noti;
import com.example.weeking.entity.Usuario;
import com.example.weeking.workers.alu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptarNoti extends RecyclerView.Adapter<AdaptarNoti.ViewHolder>{

        private List<Noti> mdata;
        private LayoutInflater minflater;
        private Context context;
        FirebaseFirestore db;


        public AdaptarNoti(List<Noti> itemlist, Context context){
            this.minflater = LayoutInflater.from(context);
            this.context =context;
            this.mdata = itemlist;
        }

        @Override
        public AdaptarNoti.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = minflater.inflate(R.layout.lista_noti, null);
            return new AdaptarNoti.ViewHolder(view);
        }
        @Override
        public int getItemCount() {
            return mdata.size();
        }
        @Override
        public void onBindViewHolder(final AdaptarNoti.ViewHolder holder, final int position){
            holder.bindData(mdata.get(position));
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView noti;
            ImageView imagen;

            ViewHolder(View itemView) {
                super(itemView);
                noti = itemView.findViewById(R.id.txt);
                imagen = itemView.findViewById(R.id.icono);
                imagen.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    Noti noti = mdata.get(position);
                    db = FirebaseFirestore.getInstance();
                    Query query = db.collection("noti").whereEqualTo("notifi", noti.getNotifi());
                    query.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();
                                Log.d("asdfg",documentId);
                                db.collection("noti").document(documentId).delete();
                            }
                        } else {
                            // Manejo de errores
                        }
                    });
                });

            }

            void bindData(final Noti item) {
                if (item != null) {
                    noti.setText(item.getNotifi());
                }
            }
        }
}
