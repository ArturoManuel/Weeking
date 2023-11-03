package com.example.weeking.workers.fragmentos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.example.weeking.entity.Usuario;
import com.example.weeking.workers.Contrasena3Activity;
import com.example.weeking.workers.viewModels.AppViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weeking.R;
import com.example.weeking.workers.AccountActivity;
import com.example.weeking.workers.StatusActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class perfil extends Fragment {
    private Button btnStatus;
    private Button btnAccount;
    private Button btnLogOut;
    private Button contrasenia;
    private FirebaseAuth mAuth;
    private AppViewModel appViewModel;
    private ImageView imageView;
    private LogoutListener logoutListener;

    private TextView userName ,codigo;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        userName = view.findViewById(R.id.textNombre); // Asume que tienes un TextView con el ID "userName" en tu layout.
        codigo = view.findViewById(R.id.textCodigoPerfil);
        mAuth = FirebaseAuth.getInstance();


        btnStatus = view.findViewById(R.id.btnStatus);
        btnStatus.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), StatusActivity.class);
            startActivity(intent);
        });

        btnAccount = view.findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(view12 -> {
            Intent intent = new Intent(getActivity(), AccountActivity.class);
            startActivity(intent);
        });

        contrasenia = view.findViewById(R.id.button3);
        contrasenia.setOnClickListener(view112 -> {
            Intent intent = new Intent(getActivity(), Contrasena3Activity.class);
            startActivity(intent);
        });
        btnLogOut = view.findViewById(R.id.btnLogout);
        btnLogOut.setOnClickListener(view13 -> {
            // Cierra la sesión con Firebase
            mAuth.signOut();

            // Notifica al listener que el usuario ha cerrado la sesión
            if (logoutListener != null) {
                logoutListener.onLogout();
            }
        });

        appViewModel.getCurrentUser().observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                // Actualizar la UI del fragmento con los datos del usuario
                actualizarUI(usuario);
            }
        });


        return view;

    }
    public interface LogoutListener {
        void onLogout();
    }

    private void actualizarUI(Usuario usuario) {

        Log.d("codigoalumno",usuario.getCodigo().toString());
        userName.setText(usuario.getNombre());
        codigo.setText(usuario.getCodigo());

    }



}