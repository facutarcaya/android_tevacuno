package com.example.micovid.pantallaprincipal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.micovid.R;
import com.example.micovid.juego.GameActivity;
import com.example.micovid.login.LoginActivity;

public class PantallaInicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio);
        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_EMAIL);
    }

    public void iniciarJuego(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}