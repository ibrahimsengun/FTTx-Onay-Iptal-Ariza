package com.example.ftthonay;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StartActivity extends AppCompatActivity {

    Button btn_ariza, btn_tesis;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore db;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        name = findViewById(R.id.tv_name);
        //Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
        db.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                        name.setText(document.getData().get("name").toString() + " " + document.getData().get("surname").toString());
                    else
                        System.out.println("-------------------- document not exists");
                }
                else
                {
                    System.out.println("--------------------- task not succes");
                }
            }
        });

        btn_ariza = findViewById(R.id.btn_ariza);
        btn_tesis = findViewById(R.id.btn_tesis);

        btn_ariza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_ariza.setEnabled(false);
                Intent intent = new Intent(StartActivity.this, CekGonderActivity.class);
                startActivity(intent);
            }
        });

        btn_tesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_tesis.setEnabled(false);
                Intent intent = new Intent(StartActivity.this, TesisActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        btn_tesis.setEnabled(true);
        btn_ariza.setEnabled(true);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setTitle("Çıkış yapmak istiyor musunuz ?")
                .setPositiveButton("Çıkış", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();
                        StartActivity.super.onBackPressed();
                    }
                })
                .setNeutralButton("Hayır", null)
                .show();
    }
}
