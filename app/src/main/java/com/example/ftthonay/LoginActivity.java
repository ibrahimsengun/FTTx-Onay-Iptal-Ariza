package com.example.ftthonay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button btn_giris;
    EditText et_giris_tc, et_giris_sifre;
    String tcNo, sifre;
    ProgressBar progressBar;

    CheckBox rememberMe;
    SharedPreferences preferences;
    private static final String PREFS_NAME = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        et_giris_sifre = findViewById(R.id.et_giris_sifre);
        et_giris_tc = findViewById(R.id.et_giris_tc);

        btn_giris = findViewById(R.id.btn_giris);

        rememberMe = findViewById(R.id.checkRemember);
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        getPrefencesData();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        et_giris_tc.requestFocus();

        btn_giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_giris_tc.getText().toString().equals("") && !et_giris_sifre.getText().toString().equals(""))
                {
                    tcNo = et_giris_tc.getText().toString() + "@turktelekom.com";
                    sifre = et_giris_sifre.getText().toString();

                    btn_giris.setEnabled(false);
                    btn_giris.setVisibility(View.INVISIBLE);

                    if (rememberMe.isChecked())
                    {
                        Boolean boolIsChecked = rememberMe.isChecked();
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("pref_tc", tcNo);
                        editor.putString("pref_pass", sifre);
                        editor.putBoolean("pref_check", boolIsChecked);
                        editor.apply();
                    }
                    else
                    {
                        preferences.edit().clear().apply();
                    }

                    progressBar.setProgress(1);
                    progressBar.setVisibility(View.VISIBLE);

                    closeKeyboard();
                    mAuth.signInWithEmailAndPassword(tcNo, sifre)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                                        startActivity(intent);

                                        btn_giris.setVisibility(View.VISIBLE);
                                        btn_giris.setEnabled(true);
                                        progressBar.setProgress(0);
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                    else
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                                                .setTitle("Hata!")
                                                .setPositiveButton("Geri", null);

                                        if (task.getException().getClass() == FirebaseAuthInvalidUserException.class)
                                        {
                                            builder.setMessage("Bilgilerinizi Kontrol Ediniz");
                                        }
                                        if(task.getException().getClass() == FirebaseAuthInvalidCredentialsException.class)
                                        {
                                            builder.setMessage("Şifrenizi Kontrol Ediniz");
                                        }
                                        builder.show();


                                        btn_giris.setEnabled(true);
                                        progressBar.setProgress(0);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        btn_giris.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }
                else
                {
                    if (et_giris_tc.getText().toString().equals(""))
                        et_giris_tc.setError("Boş bırakılamaz");
                    if(et_giris_sifre.getText().toString().equals(""))
                        et_giris_sifre.setError("Boş bırakılamaz");
                }
            }
        });
    }

    private void getPrefencesData() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_tc")) {
            String t = sp.getString("pref_tc", "not found");
            et_giris_tc.setText(t);
        }
        if(sp.contains("pref_pass")) {
            String p = sp.getString("pref_pass", "not found");
            et_giris_sifre.setText(p);
        }
        if (sp.contains("pref_check")) {
            Boolean b = sp.getBoolean("pref_check", false);
            rememberMe.setChecked(b);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        et_giris_tc.setText("");
        et_giris_sifre.setText("");

        et_giris_tc.requestFocus();
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null)
        {
            InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
