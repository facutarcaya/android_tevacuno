package com.example.micovid.juego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.micovid.R;
import com.example.micovid.login.LoginActivity;

public class GameOverActivity extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "com.example.micovid.EMAIL_OVER";
    public static final String EXTRA_TOKEN = "com.example.micovid.TOKEN_OVER";
    public static final String EXTRA_REFRESH = "com.example.micovid.REFRESH_OVER";
    public static final String EXTRA_TIEMPO = "com.example.micovid.TIEMPO_OVER";
    private int puntuacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();
        String msg_puntuacion = intent.getStringExtra(GameActivity.EXTRA_PUNTUACION);
        this.puntuacion = Integer.parseInt(msg_puntuacion);

        TextView textViewPuntuacion = findViewById(R.id.textViewPuntuacion);
        textViewPuntuacion.setText(msg_puntuacion);
    }
}