package com.example.weeking.workers.fragmentos;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.Manifest;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.weeking.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class camarafragmento extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback,EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_GALLERY_PERMISSION = 2;
    private ImageView imageView;
    private ImageButton bt_pick;
    private ImageButton bt_galera;

    //new
    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    //new
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;




    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    public camarafragmento() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_camarafragmento, container, false);

        //imageView = view.findViewById(R.id.imageView);
        //bt_galera = view.findViewById(R.id.bt_galeria);

        mButtonChooseImage = view.findViewById(R.id.galleryButton);
        imageView = view.findViewById(R.id.imagenn_view);
        mButtonUpload = view.findViewById(R.id.uploadButton);
        mTextViewShowUploads = view.findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = view.findViewById(R.id.uploadCaption);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");


        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // Botón para subir la imagen
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        // TextView para mostrar las cargas
        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }








    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE);
    }
    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void pickImageFromGallery() {

        FilePickerBuilder.getInstance()
                .setMaxCount(1)
                .pickPhoto(this);
    }


    private void requestCameraPermission() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            takePicture();
        } else {
            EasyPermissions.requestPermissions(this, "Necesitamos permisos de cámara para tomar fotos",
                    REQUEST_CAMERA_PERMISSION, perms);
        }
    }
    private void requestGalleryPermission() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            pickImageFromGallery();
        } else {
            EasyPermissions.requestPermissions(this, "Necesitamos permisos de lectura de almacenamiento para acceder a la galería", REQUEST_GALLERY_PERMISSION, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            takePicture();
        }
        if (requestCode == REQUEST_GALLERY_PERMISSION) {
            pickImageFromGallery();
        }
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this)
                        .setTitle("Permisos necesarios")
                        .setRationale("Esta aplicación necesita permisos de cámara para tomar fotos.")
                        .build()
                        .show();
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_GALLERY_PERMISSION) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this)
                        .setTitle("Permisos necesarios")
                        .setRationale("Esta aplicación necesita permisos de lectura de almacenamiento para acceder a la galería.")
                        .build()
                        .show();
            } else {
                Toast.makeText(requireContext(), "Permiso de lectura de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void mostrarImagenEnImageView(Uri imageUri) {
        try {
            Context context = requireActivity();
            ContentResolver contentResolver = context.getContentResolver();

            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);

            int targetWidth = 700;
            int targetHeight = 700;

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true);

            imageView.setImageBitmap(scaledBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) {

            return;
        }

        if (requestCode == PICK_IMAGE_REQUEST) {

            mImageUri = data.getData();
            if (imageView != null && mImageUri != null) {
                Picasso.get().load(mImageUri).into(imageView);
            } else {
                Log.e("camarafragmento", "Error: imageView es null o mImageUri es null.");
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {

            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageView != null && imageBitmap != null) {
                    imageView.setImageBitmap(imageBitmap);
                } else {
                    Log.e("camarafragmento", "Error: imageView es null o imageBitmap es null.");
                }
            }
        }
    }


    private  String getFileExtension(Uri uri) {
        ContentResolver cR = requireContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                            uri.toString());
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(upload);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}