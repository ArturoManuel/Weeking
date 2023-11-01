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
import com.example.weeking.workers.fragmentos.perfil;
import com.example.weeking.workers.viewModels.AppViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;


import com.example.weeking.Adapter.AdaptadorPrin;
import com.example.weeking.R;
import com.example.weeking.entity.ListaEven;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VistaPrincipal extends AppCompatActivity implements perfil.LogoutListener {

    private Intent intent;
    private LinearLayout layout;

    private List<ListaEven> elements;

    private ActivityVistaPrincipalBinding binding;

    FirebaseFirestore db;
    FirebaseUser currentUser;

    ListenerRegistration snapshotListener;


    private boolean isMainFragment = true;
    private int backPressCount = 0;
    private long lastBackPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        db = FirebaseFirestore.getInstance();

        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.mainFragmento);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        // Iniciar en el mainFragmento
        navController.navigate(R.id.mainFragmento);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            isMainFragment = (destination.getId() == R.id.mainFragmento);
            if (isMainFragment) {
                fab.setVisibility(View.VISIBLE);
            } else {
                fab.setVisibility(View.GONE);
            }
        });
        AppViewModel appViewModel= new ViewModelProvider(VistaPrincipal.this).get(AppViewModel.class);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Buscar el documento basado en el campo authUID que coincide con userId
            Query query = db.collection("usuarios").whereEqualTo("authUID", userId);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0); // Asume que solo hay un documento que coincide
                        String rol = document.getString("rol"); // Asume que el campo del rol se llama "rol"
                        int menuLayoutId;

                        if ("administrador".equals(rol)) {
                            menuLayoutId = R.layout.menu_personalizado_delegado_general;
                        } else if ("actividad".equals(rol)) {
                            menuLayoutId = R.layout.menu_personalizado_delegado_actividad;
                        } else if ("alumno".equals(rol)) {
                            menuLayoutId = R.layout.menu_personalizado;
                        } else {
                            Log.d(TAG, "Rol no reconocido");
                            return; // Sale de la función.
                        }
                        // Configurar el FAB con el menú correspondiente
                        fab.setOnClickListener(v -> {
                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(VistaPrincipal.this);
                            bottomSheetDialog.setContentView(menuLayoutId);
                            bottomSheetDialog.show();
                        });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        }

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

    private void navigateToActivity(Class<?> destinationClass) {
        Intent intent = new Intent(VistaPrincipal.this, destinationClass);
        startActivity(intent);
    }
    public void onDonateClick(View view) {
        Intent intent = new Intent(VistaPrincipal.this, Donacion.class);
        startActivity(intent);
    }

    public void onListaActividadesClick(View view) {
        // Tu código para manejar el click en "Lista de Actividades"
        Intent intent = new Intent(VistaPrincipal.this, ActividadesActivity.class);
        startActivity(intent);
    }

    public void onListaAlumnosClick(View view) {
        // Tu código para manejar el click en "Lista de alumnos"
        Intent intent = new Intent(VistaPrincipal.this, Lista_don.class);
        startActivity(intent);
    }

    public void onListaEstadisticasClick(View view) {
        // Tu código para manejar el click en "Estadísticas"
        Intent intent = new Intent(VistaPrincipal.this, Stadistics.class);
        startActivity(intent);
    }
    public void onListaEventosClick(View view) {
        // Tu código para manejar el click en "Lista de Eventos"
        Intent intent = new Intent(VistaPrincipal.this, EventosActivity.class);
        startActivity(intent);
    }





    @Override
    public void onLogout() {
        // Código para cerrar la sesión (Ejemplo con Firebase Authentication)
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if (isMainFragment) {
            if (backPressCount == 0 || (System.currentTimeMillis() - lastBackPressedTime > 2000)) {
                backPressCount++;
                lastBackPressedTime = System.currentTimeMillis();
            } else {
                finish();
            }
        } else {
            navController.navigate(R.id.mainFragmento);
        }
    }






}