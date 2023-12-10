package com.example.weeking.workers;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.weeking.R;
import com.example.weeking.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    ActivityMainBinding binding;
    FirebaseAuth auth;
    String canal1 = "importanteDefault";
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loguin);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Verificar si el usuario ya ha iniciado sesión
        if (auth.getCurrentUser() != null) {
            verificarEstadoUsuario(auth.getCurrentUser().getUid());
        } else {
            inicializarVista();
        }
    }

    private void verificarEstadoUsuario(String uid) {
        db.collection("usuarios").whereEqualTo("authUID", uid)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String ban = document.getString("ban");
                        if ("1".equals(ban)) {
                            iniciarVistaPrincipal();
                        } else {
                            inicializarVista();
                        }
                    } else {
                        inicializarVista();
                    }
                });
    }

    private void iniciarVistaPrincipal() {
        Intent intent = new Intent(MainActivity.this, VistaPrincipal.class);
        startActivity(intent);
        finish();
    }

    private void inicializarVista() {
        // Resto de la lógica de inicialización de vistas y eventos
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        binding.recuperarContrasena.setOnClickListener(v-> navigateToActivity(ContrasenaRecuperacion_Activity.class));
        binding.registrate.setOnClickListener(v->navigateToActivity(RegistroActivity.class));
        setContentView(binding.getRoot());
        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Query query = db.collection("usuarios").whereEqualTo("authUID", auth.getCurrentUser().getUid());
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshot = task.getResult();
                    if (!queryDocumentSnapshot.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshot.getDocuments().get(0);
                        String ban = document.getString("ban");
                        if (ban.equals("1")) {
                            // Si el usuario no está baneado, carga la VistaPrincipal
                            startActivity(new Intent(MainActivity.this, VistaPrincipal.class));
                            finish();
                        } else {
                            // Si el usuario está baneado, muestra un mensaje y no carga la VistaPrincipal
                            Toast.makeText(MainActivity.this, "La cuenta no ha sido activada o está baneada", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Manejar el caso de que no se encuentren datos del usuario
                        Toast.makeText(MainActivity.this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Manejar errores al consultar la base de datos
                    Toast.makeText(MainActivity.this, "Error al consultar la base de datos: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        crearCanalesNotificacion();
        binding.iniciarSesion.setOnClickListener(v -> {
            String correo = binding.correo.getText().toString().trim();
            String contrasena = binding.password.getText().toString();
            Query query = db.collection("usuarios").whereEqualTo("correo",correo);
            query.get().addOnCompleteListener(task ->{
                if(task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshot = task.getResult();
                    if (!queryDocumentSnapshot.isEmpty()) {
                        try{
                            DocumentSnapshot document = queryDocumentSnapshot.getDocuments().get(0);
                            String ban = document.getString("ban");
                            if(ban.equals("1")){
                                if (!correo.isEmpty() && isValidEmail(correo)) {
                                    if (!contrasena.isEmpty()) {
                                        auth.signInWithEmailAndPassword(correo, contrasena)
                                                .addOnSuccessListener(authResult -> {
                                                    notificarImportanceDefault();
                                                    startActivity(new Intent(MainActivity.this, VistaPrincipal.class));
                                                    finish();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(MainActivity.this, "Validacion incorrecta", Toast.LENGTH_SHORT).show();
                                                });
                                    } else {
                                        binding.password.setError("No se permiten campos vacíos");
                                    }} else if (correo.isEmpty()) {
                                    binding.correo.setError("No se permiten campos vacíos");
                                } else {
                                    binding.correo.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light)); // Cambia el color de fondo a rojo
                                    binding.correo.setError("Por favor, introduce un correo electrónico válido");
                                }
                            }else {
                                Toast.makeText(MainActivity.this, "La cuenta no ha sido activada o esta baneada", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Correo no registrado", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(MainActivity.this, "Correo no registrado", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        });

        binding.google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
        binding.recuperarContrasena.setOnClickListener(v -> navigateToActivity(ContrasenaRecuperacion_Activity.class));
        binding.registrate.setOnClickListener(v -> navigateToActivity(RegistroActivity.class));
        //binding.imageView.setOnClickListener(v -> navigateToActivity(ActividadesActivity.class));
        binding.bienvenidos.setOnClickListener(v -> navigateToActivity(Chat.class));
    }

    private void signInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1094221592976-b3mh285pbdrks40eg6qjjcshlfmomars.apps.googleusercontent.com")
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._%+-]+@(gmail\\.com|pucp\\.edu\\.pe|pucp\\.pe)";
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.matches(emailPattern);
    }


    private void navigateToActivity(Class<?> destinationClass) {
        Intent intent = new Intent(MainActivity.this, destinationClass);
        startActivity(intent);
    }

    public void crearCanalesNotificacion() {

        NotificationChannel channel = new NotificationChannel(canal1,
                "Canal notificaciones default",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Canal para notificaciones con prioridad default");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        pedirPermisos();
    }
    public void pedirPermisos() {
        // TIRAMISU = 33
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{POST_NOTIFICATIONS}, 101);
        }
    }

    private void notificarImportanceDefault() {
        // Crear notificación
        Intent intent = new Intent(this, VistaPrincipal.class); // Cambia esto según donde quieras que vaya el usuario al hacer clic en la notificación
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, canal1)
                .setSmallIcon(R.drawable.logo_48) // Cambia esto por tu propio icono
                .setContentTitle("Bienvenidos a Weeking")  // Título modificado
                .setContentText("Gracias por unirte a nosotros") // Texto modificado
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        Notification notification = builder.build();

        // Lanzar notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, notification);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Resultado devuelto al iniciar sesión con Google
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In fue exitoso, autenticar con Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In fallido, mostrar mensaje al usuario
                Toast.makeText(this, "Google Sign In fallido", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in con Google exitoso, verificar el estado del usuario
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                verificarEstadoUsuario(user.getUid());
                            }
                        } else {
                            // Si falla el inicio de sesión con Firebase, mostrar mensaje al usuario
                            Toast.makeText(MainActivity.this, "Autenticación con Firebase fallida", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}