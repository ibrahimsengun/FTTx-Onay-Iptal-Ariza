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

public class IptalActivity extends AppCompatActivity {

    Spinner spinner;
    ProgressBar progressBar;
    String iptalSebep, iptalAciklama, s;
    EditText multiLineText, et_hizmetNo;
    Button btn_gonder;
    long hizmetNo;

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iptal);

        btn_gonder = findViewById(R.id.btn_iptal_gonder);
        multiLineText = findViewById(R.id.multilineText);
        et_hizmetNo = findViewById(R.id.hzmtNo);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();

        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);

        spinner = findViewById(R.id.spinner_iptal);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.IPTALSEBEP, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] sebepler = getResources().getStringArray(R.array.IPTALSEBEP);
                iptalSebep = sebepler[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iptalAciklama = String.valueOf(multiLineText.getText());
                final AlertDialog.Builder builder = new AlertDialog.Builder(IptalActivity.this);

                if(et_hizmetNo.getText().toString().length() < 10)
                {
                    et_hizmetNo.setError("10 basamaklı hizmet numarasını giriniz");
                }
                else if(iptalSebep.equals("Seçiniz"))
                {
                    builder.setTitle("Hata!")
                            .setMessage("Sebep seçiniz!")
                            .setPositiveButton("OK", null).show();
                }
                else
                {
                    hizmetNo = Long.parseLong(et_hizmetNo.getText().toString());
                    builder.setTitle("Onaylıyor musunuz?");
                    if(!iptalAciklama.equals(""))
                        builder.setMessage("Hizmet No: " + hizmetNo + "\n" +
                                "İptal Sebebi: " + iptalSebep + "\n" +
                                "Açıklama: " + iptalAciklama)
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

                                        IptalIslem cancel = new IptalIslem(String.valueOf(hizmetNo), iptalSebep, iptalAciklama, tarih);
                                        db.collection("Users").document(user.getUid()).
                                                collection("Cancels").document(Long.toString(hizmetNo)).set(cancel)
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
                                });
                    else
                        builder.setMessage("Hizmet No: " + hizmetNo + "\n" +
                                "İptal Sebebi: " + iptalSebep + "\n" +
                                "Açıklama: Yapılmadı")
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

                                        IptalIslem cancel = new IptalIslem(String.valueOf(hizmetNo), iptalSebep, tarih);
                                        db.collection("Users").document(user.getUid()).
                                                collection("Cancels").document(Long.toString(hizmetNo)).set(cancel)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        btn_gonder.setVisibility(View.VISIBLE);
                                                        btn_gonder.setEnabled(true);
                                                        progressBar.setProgress(0);
                                                        progressBar.setVisibility(View.INVISIBLE);

                                                        db.collection("Users").document(user.getUid())
                                                                .collection("Tesis").document(String.valueOf(hizmetNo)).delete();

                                                        storage.child("IsEmirleri").child(s).delete();

                                                        finish();
                                                    }
                                                });
                                    }
                                });
                    builder.setNeutralButton("Hayır", null).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        s = intent.getStringExtra("HIZMETNO");
        et_hizmetNo.setText(s);
    }
}