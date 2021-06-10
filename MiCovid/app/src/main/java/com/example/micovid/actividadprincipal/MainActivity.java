package com.example.micovid.actividadprincipal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.micovid.R;
import com.example.micovid.login.LoginActivity;
import com.example.micovid.registrar.RegistrarActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void abrirLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void abrirRegister(View view) {
        Intent intent = new Intent(this, RegistrarActivity.class);
        startActivity(intent);
    }

}