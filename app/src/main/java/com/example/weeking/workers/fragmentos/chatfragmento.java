package com.example.weeking.workers.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weeking.Adapter.AdaptadorE;
import com.example.weeking.Adapter.AdaptadorPrin;
import com.example.weeking.Adapter.AdapterMensajes;
import com.example.weeking.R;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.ListaEven;
import com.example.weeking.entity.Mensaje;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class chatfragmento extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListaEven> elements;
    List<EventoClass> listaEven = new ArrayList<>();

    public chatfragmento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.lista_chat);
        // Configurar el RecyclerView y el adaptador
        elements = new ArrayList<>();
        AdaptadorE listaAdapter = new AdaptadorE(listaEven,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Eventos").orderBy("fecha_evento", Query.Direction.DESCENDING).addSnapshotListener((collection, error) -> {
            if (error != null) {
                Log.d("lectura", "Error listening for document changes.");
                return;
            }
            if (collection != null && !collection.isEmpty()) {
                listaEven.clear();
                for (QueryDocumentSnapshot document1 : collection) {
                    try {
                        EventoClass eve = document1.toObject(EventoClass.class);
                        listaEven.add(eve);
                    } catch (Exception e) {
                        Log.e("asdf", "Error processing document", e);
                    }
                }
                listaAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(listaAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatfragmento, container, false);
    }
}