package com.example.ftthonay;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ArizaActivity extends AppCompatActivity {

    EditText et_hizmetNo, et_aciklama;
    Spinner spn_grup, spn_kod;
    Button btn_gonder;
    ProgressBar progressBar;

    String grup, kod, s;
    int pos;

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ariza);

        et_hizmetNo = findViewById(R.id.ariza_hizmetNo);
        et_aciklama = findViewById(R.id.et_aciklama3);
        spn_grup = findViewById(R.id.spn_grup);
        spn_kod = findViewById(R.id.spn_kod);
        btn_gonder = findViewById(R.id.btn_arzGonder);

        progressBar = findViewById(R.id.progressBar5);
        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.KODGRUBU, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_grup.setAdapter(adapter);

        spn_grup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] gruplar = getResources().getStringArray(R.array.KODGRUBU);
                ArrayAdapter adapter1 = null;
                if(position == 0)
                {
                    adapter1 = ArrayAdapter.createFromResource(ArizaActivity.this, R.array.KOD1, android.R.layout.simple_spinner_item);
                }
                else if(position == 1)
                {
                    adapter1 = ArrayAdapter.createFromResource(ArizaActivity.this, R.array.KOD2, android.R.layout.simple_spinner_item);
                }
                else if(position == 2)
                {
                    adapter1 = ArrayAdapter.createFromResource(ArizaActivity.this, R.array.KOD3, android.R.layout.simple_spinner_item);
                }
                else if(position == 3)
                {
                    adapter1 = ArrayAdapter.createFromResource(ArizaActivity.this, R.array.KOD4, android.R.layout.simple_spinner_item);
                }
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_kod.setAdapter(adapter1);
                grup = gruplar[position];
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_kod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] kodlar = new String[3];
                if(pos == 0)
                {
                    kodlar = getResources().getStringArray(R.array.KOD1);
                }
                else if(pos == 1)
                {
                    kodlar = getResources().getStringArray(R.array.KOD2);
                }
                else if (pos == 2)
                {
                    kodlar = getResources().getStringArray(R.array.KOD3);
                }
                else if(pos == 3)
                {
                    kodlar = getResources().getStringArray(R.array.KOD4);
                }
                kod = kodlar[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String hizmet_no = et_hizmetNo.getText().toString();
                final String aciklama = et_aciklama.getText().toString();

                if(hizmet_no.length() < 10)
                {
                    et_hizmetNo.setError("10 basamaklı hizmet numarasını giriniz");
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ArizaActivity.this);
                    builder.setTitle("Onaylıyor musunuz ?")
                            .setMessage("Hizmet No: " + hizmet_no + "\n" +
                                    "Grup: " + grup + "\n" +
                                    "Kod: " + kod + "\n" +
                                    "Açıklama: " + aciklama)
                            .setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    btn_gonder.setVisibility(View.INVISIBLE);
                                    btn_gonder.setEnabled(false);
                                    progressBar.setProgress(1);
                                    progressBar.setVisibility(View.VISIBLE);

                                    user = mAuth.getCurrentUser();

                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
                                    String tarih = format.format(c.getTime());

                                    Ariza ariza = new Ariza(hizmet_no, grup, kod, aciklama, tarih);

                                    db.collection("Users").document(user.getUid())
                                            .collection("Ariza").document(hizmet_no).set(ariza)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            btn_gonder.setVisibility(View.VISIBLE);
                                            btn_gonder.setEnabled(true);
                                            progressBar.setProgress(0);
                                            progressBar.setVisibility(View.INVISIBLE);

                                            db.collection("Users").document(user.getUid())
                                                    .collection("Tesis").document(s).delete();

                                            storage.child("IsEmirleri").child(s).delete();

                                            finish();
                                        }
                                    });
                                }
                            }).setNeutralButton("Hayır", null).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();

        Intent intent = getIntent();
        s = intent.getStringExtra("HIZMETNO");
        et_hizmetNo.setText(s);
    }
}
