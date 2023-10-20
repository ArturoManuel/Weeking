package com.example.weeking.workers;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.weeking.databinding.ActivityVistaPrincipalBinding;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.EventoDto;
import com.example.weeking.workers.fragmentos.mainFragmento;
import com.example.weeking.workers.viewModels.AppViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;


import com.example.weeking.Adapter.AdaptadorPrin;
import com.example.weeking.R;
import com.example.weeking.entity.ListaEven;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VistaPrincipal extends AppCompatActivity {

    private Intent intent;
    private LinearLayout layout;

    private List<ListaEven> elements;

    private ActivityVistaPrincipalBinding binding;

    FirebaseFirestore db;

    ListenerRegistration snapshotListener;

    private boolean isMainFragment = true;
    private int backPressCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal);




        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.mainFragmento);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        // Iniciar en el mainFragmento
        navController.navigate(R.id.mainFragmento);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> isMainFragment = (destination.getId() == R.id.mainFragmento));

        AppViewModel appViewModel= new ViewModelProvider(VistaPrincipal.this).get(AppViewModel.class);
        db = FirebaseFirestore.getInstance();
        db.collection("Eventos").addSnapshotListener((collection, error) -> {
            if (error != null) {
                Log.w(TAG, "Error listening for document changes.", error);
                return;
            }
            if (collection != null && !collection.isEmpty()) {
                List<EventoClass> eventos = new ArrayList<>();
                for (QueryDocumentSnapshot document : collection) {
                    EventoClass evento = document.toObject(EventoClass.class);
                    eventos.add(evento);
                }
                appViewModel.getListaEventos().postValue(eventos);
            }
        });

    }
    public void onBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        if (isMainFragment) {
            if (backPressCount == 0) {
                // Si ya estamos en el mainFragmento y es la primera vez que se presiona, cerrar la actividad
                finish();
            } else {
                // Resetea el contador
                backPressCount = 0;
            }
        } else {
            // Intenta volver al fragmento anterior
            if (!navController.popBackStack()) {
                // Si no se puede, ve al mainFragmento
                navController.navigate(R.id.mainFragmento);
            } else {
                // Incrementa el contador porque logramos regresar al fragmento anterior
                backPressCount++;
            }
        }
    }



    private void setupImageSlider(ImageSlider imageSlider) {
        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel("https://drive.google.com/file/d/1Zna5-06QK4mboQ3nVOHBOs-dBf_HuR_N/view?usp=drive_link", "Inauguran el “XVII Festival de Teatro Saliendo de la Caja” en el Centro Cultural PUCP", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://drive.google.com/file/d/1kaW0CsG51sfXP0JfEUgsnOesGHTzTBr4/view?usp=drive_link", "Torneos exclusivos de la fibra", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://drive.google.com/file/d/1Jpzx9V7z5PO_2sCJA5Nd60-GNm9g7kMu/view?usp=drive_link", "Sábado el baileton", ScaleTypes.CENTER_CROP));

        // Configura el ImageSlider pasado como parámetro
        imageSlider.setImageList(imageList);
    }


}