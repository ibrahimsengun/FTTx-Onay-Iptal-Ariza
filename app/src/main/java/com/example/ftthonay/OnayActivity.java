package com.example.ftthonay;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OnayActivity extends AppCompatActivity {

    Button btn_ileri, btn_hgw, btn_ont;
    EditText tv_hgw, tv_ont;
    EditText et_hizmetNo, et_Aciklama;
    Spinner spinnerONT, spinnerHGW;
    ProgressBar progressBar;

    String ONTModel = "", HGWModel = "", s, himzet;
    long hizmetNo;
    int taranan;
    boolean isReadHGW = false, isReadONT = false;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore db;
    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onay);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();

        et_hizmetNo = findViewById(R.id.hizmetNo);
        et_Aciklama = findViewById(R.id.et_aciklama);

        tv_hgw = findViewById(R.id.tv_hgw);
        tv_ont = findViewById(R.id.tv_ont);

        btn_hgw = findViewById(R.id.btn_hgw);
        btn_ont = findViewById(R.id.btn_ont);

        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        spinnerONT = findViewById(R.id.spinner_ont);
        spinnerHGW = findViewById(R.id.spinner_hgw);

        ArrayAdapter adapterONT = ArrayAdapter.createFromResource(this, R.array.ONTMODEL, android.R.layout.simple_spinner_item);
        adapterONT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerONT.setAdapter(adapterONT);

        ArrayAdapter adapterHGW = ArrayAdapter.createFromResource(this, R.array.HGWMODEL, android.R.layout.simple_spinner_item);
        adapterHGW.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHGW.setAdapter(adapterHGW);

        spinnerONT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] model = getResources().getStringArray(R.array.ONTMODEL);
                if(position == 0)
                {
                    btn_ont.setEnabled(false);
                    btn_ont.setBackgroundColor(getResources().getColor(R.color.colorDisableButton));
                }
                else
                {
                    btn_ont.setEnabled(true);
                    btn_ont.setBackgroundColor(getResources().getColor(R.color.colorEnableButton));
                    ONTModel = model[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerHGW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] model = getResources().getStringArray(R.array.HGWMODEL);
                btn_hgw.setEnabled(true);
                btn_hgw.setBackgroundColor(getResources().getColor(R.color.colorEnableButton));
                HGWModel = model[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Intent intent = getIntent();
        s = intent.getStringExtra("HIZMETNO");
        et_hizmetNo.setText(s);

        btn_ileri = findViewById(R.id.btn_ileri);

        btn_ileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(OnayActivity.this);

                if(et_hizmetNo.getText().toString().length() < 10)
                {
                    et_hizmetNo.setError("10 haneli kodu giriniz");
                    /*builder.setTitle("Hata!")
                            .setMessage("10 basamaklı hizmet numarasını giriniz")
                            .setPositiveButton("OK", null).show();*/
                }
                else if(tv_hgw.getText().equals(""))
                {
                    tv_hgw.setError("Barkod okutulmadı");
                }
                else
                {
                    hizmetNo = Long.parseLong(et_hizmetNo.getText().toString());
                    if(ONTModel.equals(""))
                    {
                        builder.setTitle("Onaylıyor musunuz?")
                                .setMessage("Hizmet No:" + hizmetNo + "\n" +
                                        "HGW:\nModel " + HGWModel + " - Kod: " + tv_hgw.getText() + "\n" +
                                        "ONT: Yok")
                                .setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        btn_ileri.setVisibility(View.INVISIBLE);
                                        btn_ileri.setEnabled(false);
                                        progressBar.setProgress(1);
                                        progressBar.setVisibility(View.VISIBLE);

                                        Calendar c = Calendar.getInstance();
                                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
                                        String tarih = format.format(c.getTime());

                                        OnayIslem confirm = new OnayIslem(et_hizmetNo.getText().toString(), HGWModel, tv_hgw.getText().toString(), et_Aciklama.getText().toString(), tarih, "0", "yok");
                                        db.collection("Users").document(currentUser.getUid())
                                                .collection("Confirms").document(Long.toString(hizmetNo)).set(confirm)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        btn_ileri.setVisibility(View.VISIBLE);
                                                        btn_ileri.setEnabled(true);
                                                        progressBar.setProgress(0);
                                                        progressBar.setVisibility(View.INVISIBLE);

                                                        db.collection("Users").document(currentUser.getUid())
                                                                .collection("Tesis").document(s).delete();

                                                        storage.child("IsEmirleri").child(s).delete();

                                                        finish();
                                                    }
                                                });
                                    }
                                })
                                .setNeutralButton("Hayır", null).show();
                    }
                    else
                    {
                        if(!tv_ont.getText().equals(""))
                        {
                            builder.setTitle("Onaylıyor musunuz?")
                                    .setMessage("Hizmet No: " + hizmetNo + "\n" +
                                            "HGW:\nModel " + HGWModel + " - Kod: " + tv_hgw.getText() + "\n" +
                                            "ONT\nModel: " + ONTModel + " - Kod: " + tv_ont.getText() + "\n" +
                                            "Açıklama: " + et_Aciklama.getText().toString())
                                    .setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            btn_ileri.setVisibility(View.INVISIBLE);
                                            btn_ileri.setEnabled(false);
                                            progressBar.setProgress(1);
                                            progressBar.setVisibility(View.VISIBLE);

                                            Calendar c = Calendar.getInstance();
                                            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
                                            String tarih = format.format(c.getTime());

                                            OnayIslem confirm = new OnayIslem(et_hizmetNo.getText().toString(), HGWModel, tv_hgw.getText().toString(),
                                                    ONTModel, tv_ont.getText().toString(), et_Aciklama.getText().toString(), tarih, "0", "yok");
                                            db.collection("Users").document(currentUser.getUid())
                                                    .collection("Confirms").document(Long.toString(hizmetNo)).set(confirm)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            btn_ileri.setVisibility(View.VISIBLE);
                                                            btn_ileri.setEnabled(true);
                                                            progressBar.setProgress(0);
                                                            progressBar.setVisibility(View.INVISIBLE);

                                                            db.collection("Users").document(currentUser.getUid())
                                                                    .collection("Tesis").document(s).delete();

                                                            storage.child("IsEmirleri").child(s).delete();

                                                            finish();
                                                        }
                                                    });
                                            //ref.child(currentUser.getUid()).child("confirms").child(String.valueOf(hizmetNo)).setValue(confirm);
                                        }
                                    })
                                    .setNeutralButton("Hayır", null).show();
                        }
                        else
                        {
                            builder.setTitle("Hata!")
                                    .setMessage("ONT Barkod okutulmadı!")
                                    .setPositiveButton("OK", null).show();
                        }
                    }
                }
            }
        });

        btn_hgw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                himzet = et_hizmetNo.getText().toString();
                IntentIntegrator integrator = new IntentIntegrator(OnayActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        .setPrompt("HGW")
                        .setCameraId(0)
                        .setCaptureActivity(AnyOrientationCaptureActivity.class)
                        .setOrientationLocked(true)
                        .setBeepEnabled(true)
                        .setBarcodeImageEnabled(false)
                        .initiateScan();
                taranan = 1;
            }
        });

        btn_ont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                himzet = et_hizmetNo.getText().toString();
                IntentIntegrator integrator = new IntentIntegrator(OnayActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        .setPrompt("ONT")
                        .setCameraId(0)
                        .setCaptureActivity(AnyOrientationCaptureActivity.class)
                        .setOrientationLocked(true)
                        .setBeepEnabled(true)
                        .setBarcodeImageEnabled(true)
                        .initiateScan();
                taranan = 2;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("------ onStart ------");
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OnayActivity.this);
            builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (taranan == 1) {
                        tv_hgw.setText(result.getContents());
                        isReadHGW = true;
                    } else if (taranan == 2) {
                        tv_ont.setText(result.getContents());
                        isReadONT = true;
                    }
                }
            }).setNeutralButton("Tekrar Dene", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (taranan == 1) {
                        btn_hgw.callOnClick();
                        isReadHGW = false;
                    } else if (taranan == 2) {
                        btn_ont.callOnClick();
                        isReadONT = false;
                    }
                }
            })
                    .setMessage(result.getContents())
                    .show();
        }
        et_hizmetNo.setText(himzet);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
