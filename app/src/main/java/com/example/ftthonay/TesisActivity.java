package com.example.ftthonay;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TesisActivity extends AppCompatActivity {

    ListView listView;
    PhotoView image;
    TextView textView;

    String hizmet_no = "";

    Button btn_onay, btn_iptal, btn_ariza;

    List<Tesis> tesisList = new ArrayList<>();
    Adapter adapter;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tesis);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btn_iptal = findViewById(R.id.btn_iptal);
        btn_onay = findViewById(R.id.btn_onay);
        btn_ariza = findViewById(R.id.btn_arz);

        btn_iptal.setVisibility(View.INVISIBLE);
        btn_onay.setVisibility(View.INVISIBLE);
        btn_ariza.setVisibility(View.INVISIBLE);

        listView = findViewById(R.id.listview);
        image = findViewById(R.id.tesisImage);

        textView = findViewById(R.id.tx_emptyList);
        textView.setVisibility(View.INVISIBLE);

        currentUser = mAuth.getCurrentUser();

        btn_onay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_onay.setEnabled(false);
                Intent intent = new Intent(TesisActivity.this, OnayActivity.class);
                intent.putExtra("HIZMETNO", hizmet_no);
                startActivity(intent);
                finish();
            }
        });

        btn_iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_iptal.setEnabled(false);
                Intent intent = new Intent(TesisActivity.this, IptalActivity.class);
                intent.putExtra("HIZMETNO", hizmet_no);
                startActivity(intent);
                finish();
            }
        });

        btn_ariza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_ariza.setEnabled(false);
                Intent intent = new Intent(TesisActivity.this, ArizaActivity.class);
                intent.putExtra("HIZMETNO", hizmet_no);
                startActivity(intent);
                finish();
            }
        });

        RefreshList();

        listView.setEmptyView(textView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btn_onay.setVisibility(View.VISIBLE);
                btn_iptal.setVisibility(View.VISIBLE);
                btn_ariza.setVisibility(View.VISIBLE);
                Tesis tesis = tesisList.get(position);
                Picasso.get().load(tesis.getImageURL()).into(image);

                hizmet_no = tesis.getImageName();
            }
        });
    }

    public void RefreshList()
    {
        db.collection("Users").document(currentUser.getUid()).collection("Tesis")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String isim = "bu bos", tarih = "bu bos";
                        Uri url = null;
                        Map<String, Object> map = new HashMap<>();
                        map = document.getData();
                        Tesis tesis;
                        for (String keys: map.keySet()) {
                            if(keys.equals("fotograf"))
                            {
                                url = Uri.parse((String) map.get(keys));
                            }
                            if (keys.equals("isim"))
                            {
                                isim = (String) map.get(keys);
                            }
                            if(keys.equals("tarih"))
                            {
                                tarih = (String) map.get(keys);
                            }
                        }
                        tesis = new Tesis(url, tarih, isim);
                        tesisList.add(tesis);
                        adapter = new Adapter(getApplicationContext(), tesisList);
                        listView.setAdapter(adapter);
                    }
                } else {
                    System.out.println("Error getting documents: " + task.getException());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("***************** ONRESETART ------------");
        RefreshList();
        btn_onay.setEnabled(true);
        btn_iptal.setEnabled(true);
        btn_ariza.setEnabled(true);
    }
}
