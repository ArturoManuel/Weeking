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

import com.example.weeking.workers.Contrasena3Activity;
import com.google.firebase.auth.FirebaseAuth;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.weeking.R;
import com.example.weeking.workers.AccountActivity;
import com.example.weeking.workers.StatusActivity;

public class perfil extends Fragment {

    private Button btnStatus;
    private Button btnAccount;
    private Button btnLogOut;
    private Button contrasenia;
    private ImageView imageView;
    private LogoutListener logoutListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LogoutListener) {
            logoutListener = (LogoutListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement LogoutListener");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

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
        btnLogOut.setOnClickListener(view13 -> logoutListener.onLogout());
        return view;

    }
    public interface LogoutListener {
        void onLogout();
    }

}