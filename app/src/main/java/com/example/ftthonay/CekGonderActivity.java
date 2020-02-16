package com.example.ftthonay;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CekGonderActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 1001;
    private static final int CHOOSE_PHOTO_CODE = 1002;
    private static final int PERMISSION_CODE = 1000;

    private StorageReference mStorage;

    Button btn_foto, btn_gonder, btn_galeri;
    EditText et_aciklama;
    Spinner spinner;
    TextView tx_fotograf;
    ImageView image;
    ProgressBar progressBar;

    boolean isSuccess = false;
    long hizmetNo;
    String is_turu;
    private String image_url;

    Uri image_uri;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cekgonder);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mStorage = FirebaseStorage.getInstance().getReference();

        btn_foto = findViewById(R.id.btn_photo);
        btn_gonder = findViewById(R.id.btn_arizaGonder);
        btn_galeri = findViewById(R.id.btn_galeri);
        et_aciklama = findViewById(R.id.et_arizaAciklama);
        tx_fotograf = findViewById(R.id.tx_fotograf);
        spinner = findViewById(R.id.spinner_ariza);
        progressBar = findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.INVISIBLE);
        image = findViewById(R.id.image);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.ISTURU, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] model = getResources().getStringArray(R.array.ISTURU);
                is_turu = model[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else
                    {
                        openCamera();
                    }
                }
                else
                {
                    openCamera();
                }
            }
        });

        btn_galeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                startActivityForResult(Intent.createChooser(intent, "Fotoğraf seç"), CHOOSE_PHOTO_CODE);
            }
        });

        btn_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSuccess)
                {
                    AlertDialog builder = new AlertDialog.Builder(CekGonderActivity.this)
                            .setTitle("Hata")
                            .setMessage("Fotoğraf yüklenmedi")
                            .setPositiveButton("OK", null)
                            .show();
                }
                else
                {
                    progressBar.setProgress(1);
                    progressBar.setVisibility(View.VISIBLE);
                    btn_gonder.setVisibility(View.INVISIBLE);

                    final StorageReference filepath = mStorage.child("Photos").child(image_uri.getLastPathSegment());
                    final UploadTask uploadTask = filepath.putFile(image_uri);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mStorage.child("Photos").child(image_uri.getLastPathSegment()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
                                    String tarih = format.format(c.getTime());

                                    CekGonder cekGonder = new CekGonder(is_turu, task.getResult().toString(), et_aciklama.getText().toString(), tarih);

                                    db.collection("Users").document(currentUser.getUid())
                                            .collection("CekGonder").document(image_uri.getLastPathSegment())
                                            .set(cekGonder)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    progressBar.setProgress(0);
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    btn_gonder.setVisibility(View.VISIBLE);
                                                    finish();
                                                }
                                            });
                                }
                            });
                        }
                    });

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
                    String tarih = format.format(c.getTime());

                    CekGonder cekGonder = new CekGonder(is_turu, image_url, et_aciklama.getText().toString(), tarih);

                    db.collection("Users").document(currentUser.getUid())
                            .collection("CekGonder").document(image_uri.getLastPathSegment())
                            .set(cekGonder)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    finish();
                                }
                            });

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            isSuccess = true;
            image.setImageURI(image_uri);
        }
        if(requestCode == CHOOSE_PHOTO_CODE && resultCode == RESULT_OK) {
            String fileName = parseUriToFilename(data.getData());

            if (fileName != null)
            {
                isSuccess = true;
                image_uri = data.getData();
                image.setImageURI(data.getData());
            }
        }
    }

    public String parseUriToFilename(Uri uri) {
        String selectedImagePath = null;
        String filemanagerPath = uri.getPath();

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);

        if (cursor != null) {
            // Here you will get a null pointer if cursor is null
            // This can be if you used OI file manager for picking the media
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            selectedImagePath = cursor.getString(column_index);
        }

        if (selectedImagePath != null) {
            return selectedImagePath;
        }
        else if (filemanagerPath != null) {
            return filemanagerPath;
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
                else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
