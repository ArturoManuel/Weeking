package com.example.weeking.workers;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.weeking.Adapter.AdaptadorDon;
import com.example.weeking.Adapter.AdaptarNoti;
import com.example.weeking.databinding.ActivityVistaPrincipalBinding;
import com.example.weeking.entity.EventoClass;
import com.example.weeking.entity.EventoDto;
import com.example.weeking.entity.ListaDon;
import com.example.weeking.entity.Noti;
import com.example.weeking.entity.Usuario;
import com.example.weeking.workers.fragmentos.mainFragmento;
import com.example.weeking.workers.fragmentos.mapafragmento;
import com.example.weeking.workers.fragmentos.perfil;
import com.example.weeking.workers.viewModels.AppViewModel;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VistaPrincipal extends AppCompatActivity implements perfil.LogoutListener , mapafragmento.OnMapReadyListener {

    private Intent intent;
    private LinearLayout layout;

    private List<ListaEven> elements;

    private ActivityVistaPrincipalBinding binding;

    FirebaseFirestore db;
    FirebaseUser currentUser;
    int a = 1;

    ListenerRegistration snapshotListener;

    TextView nombre, estado;

    private String codigo;
    private boolean isMainFragment = true;
    private int backPressCount = 0;
    private long lastBackPressedTime;


    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    MaterialToolbar toolbar;

    AppViewModel appViewModel;

    private ProgressBar progressBar;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal);
        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        fab = findViewById(R.id.floatingActionButton);
        db = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            isMainFragment = (destination.getId() == R.id.mainFragmento);
            if (isMainFragment) {
                fab.setVisibility(View.VISIBLE);
            } else {
                fab.setVisibility(View.GONE);
            }

            if (destination.getId() == R.id.mapafragmento) {

                mostrarMapaFragmentoCuandoEsteListo();
            }
        });


        appViewModel= new ViewModelProvider(VistaPrincipal.this).get(AppViewModel.class);
        appViewModel.iniciarListenerEventos();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        if (currentUser != null) {
            cargarDatosUsuario(currentUser.getUid());
        }



    }

    private void mostrarMapaFragmentoCuandoEsteListo() {
        mostrarIndicadorDeProgreso();
        executorService.execute(() -> {
            runOnUiThread(() -> {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mapFragment);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(googleMap -> {
                        ocultarIndicadorDeProgreso();
                    });
                }
            });
        });
    }


    private void mostrarIndicadorDeProgreso() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void ocultarIndicadorDeProgreso() {
        progressBar.setVisibility(View.GONE);
    }
    @Override
    public void onMapReady() {
        ocultarIndicadorDeProgreso();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    private void cargarDatosUsuario(String userId) {
        Query query = db.collection("usuarios").whereEqualTo("authUID", userId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0); // Asume que solo hay un documento que coincide
                    String rol = document.getString("rol"); // Asume que el campo del rol se llama "rol"
                    int menuLayoutId;
                    obtenerCodigoDelAlumno(userId);
                    if ("administrador".equals(rol)) {
                        menuLayoutId = R.layout.menu_personalizado_delegado_general;
                        toolbar.setTitle("Weeking - Administrador");
                    } else if ("delegado_de_actividad".equals(rol)) {
                        menuLayoutId = R.layout.menu_personalizado_delegado_actividad;
                        toolbar.setTitle("Weeking - Delegado");
                    } else if ("alumno".equals(rol)) {
                        menuLayoutId = R.layout.menu_personalizado;
                        toolbar.setTitle("Weeking - Alumno");
                    } else {
                        Log.d(TAG, "Rol no reconocido");
                        return; // Sale de la función.
                    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent1 = new Intent(this, Notificacion.class);
        startActivity(intent1);
        return super.onOptionsItemSelected(item);
    }

    private void obtenerCodigoDelAlumno(String authID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection("usuarios");

        Query query = colRef.whereEqualTo("authUID", authID);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                String codigoAlumno = document.getId();
                // Aquí obtenemos el código del alumno, que es el ID del documento
                Log.d("codigoencontrado",codigoAlumno);

                cargarDatosUsuarioDesdeFirestore(codigoAlumno);
            } else {
                Log.d("mensajeError","no se encontro el codigo");
            }
        }).addOnFailureListener(e -> {
            // Maneja cualquier error que ocurra al tratar de obtener el documento.
            Log.d("fallo en encontrar el documento","no se encontro");
        });
    }


    private void cargarDatosUsuarioDesdeFirestore(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Usuario usuario = documentSnapshot.toObject(Usuario.class);
                    if (usuario != null) {
                        // Guarda el usuario en AppViewModel
                        Log.d("datos",usuario.getNombre());
                        appViewModel.setCurrentUser(usuario);

                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(VistaPrincipal.this, "Error al cargar datos del usuario.", Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToActivity(Class<?> destinationClass) {
        Intent intent = new Intent(VistaPrincipal.this, destinationClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onDonateClick(View view) {
        view.setEnabled(false); // Deshabilitar el botón

        new Handler().postDelayed(() -> view.setEnabled(true), 1000);
        Intent intent = new Intent(VistaPrincipal.this, Donacion.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onListaActividadesClick(View view) {
        view.setEnabled(false); // Deshabilitar el botón

        new Handler().postDelayed(() -> view.setEnabled(true), 1000);
        // Tu código para manejar el click en "Lista de Actividades"
        Intent intent = new Intent(VistaPrincipal.this, ActividadesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onListaAlumnosClick(View view) {
        view.setEnabled(false); // Deshabilitar el botón

        new Handler().postDelayed(() -> view.setEnabled(true), 1000);
        // Tu código para manejar el click en "Lista de alumnos"
        Intent intent = new Intent(VistaPrincipal.this, Lista_don.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onListaEstadisticasClick(View view) {
        view.setEnabled(false); // Deshabilitar el botón

        new Handler().postDelayed(() -> view.setEnabled(true), 1000);
        // Tu código para manejar el click en "Estadísticas"
        Intent intent = new Intent(VistaPrincipal.this, Stadistics.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void onListaEventosClick(View view) {
        view.setEnabled(false); // Deshabilitar el botón

        String currentUserId = null;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        new Handler().postDelayed(() -> view.setEnabled(true), 1000);

        // Crear el Intent para iniciar EventosActivity
        Intent intent = new Intent(VistaPrincipal.this, EventosActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        // Poner el UID como un extra en el Intent
        if (currentUserId != null) {
            intent.putExtra("USER_ID", currentUserId);
            intent.putExtra("CODIGO",appViewModel.getCurrentUser().getValue().getCodigo());

        }

        // Iniciar la actividad
        startActivity(intent);
    }





    @Override
    public void onLogout() {
        // Código para cerrar la sesión (Ejemplo con Firebase Authentication)
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appViewModel.detenerListenerEventos();  // Detiene la escucha de eventos
        executorService.shutdown();
    }






}