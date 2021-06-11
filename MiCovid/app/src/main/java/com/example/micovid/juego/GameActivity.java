package com.example.micovid.juego;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.micovid.R;
import com.example.micovid.asincronico.AsincroTaskLogin;
import com.example.micovid.asincronico.AsincroTaskVerificarPos;
import com.example.micovid.login.LoginActivity;

public class GameActivity extends AppCompatActivity {

    private static final int POS_MINIMA = -5;
    private static final int POS_MAXIMA = 5;
    private RotateAnimation rotateAnimation;
    private TextView textViewPuntuacion2;
    private ImageView imageViewVacunaVerde;
    private ImageView imageViewVacunaRoja;
    private float anguloActual;
    private int puntuacion;
    public boolean validando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        puntuacion = 0;
        validando = false;

        this.imageViewVacunaVerde = findViewById(R.id.imageViewVacunaVerde);
        this.imageViewVacunaRoja = findViewById(R.id.imageViewVacunaRoja);
        this.textViewPuntuacion2 = findViewById(R.id.textViewPuntuacion2);

        reiniciarPos();
        actualizarPuntuacion();
    }

    public void rotarMas(View view) {
        rotateImage(5);
    }

    public void rotarMenos(View view) {
        rotateImage(-5);
    }

    public void rotateImage(int degrees) {
        if (anguloActual + degrees > 90 || anguloActual + degrees < -90) {
            degrees = 0;
        }
        this.rotateAnimation = new RotateAnimation(anguloActual, anguloActual + degrees,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        this.rotateAnimation.setInterpolator(new LinearInterpolator());
        this.rotateAnimation.setDuration(100);
        this.rotateAnimation.setRepeatCount(0);
        this.rotateAnimation.setFillAfter(true);
        this.imageViewVacunaRoja.startAnimation(this.rotateAnimation);
        Log.d("Angulos", "De: " + this.anguloActual + " A: " + (anguloActual + degrees));
        this.anguloActual = anguloActual + degrees;
        if (!validando) {
            validarPos();
        }
    }

    public void validarPos() {
        if(verificarPos()) {
            lanzarProcesoVerificacion();
        }
    }

    public void reiniciarPos() {

        int posRandom = getRandomNumber(0,40);

        if (getRamdomBool()) {
            posRandom = -90 + posRandom;
        } else {
            posRandom = 50 + posRandom;
        }
        this.rotateAnimation = new RotateAnimation(0, posRandom,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        this.rotateAnimation.setInterpolator(new LinearInterpolator());
        this.rotateAnimation.setDuration(1);
        this.rotateAnimation.setRepeatCount(0);
        this.rotateAnimation.setFillAfter(true);
        imageViewVacunaRoja.startAnimation(this.rotateAnimation);
        this.anguloActual = posRandom;
    }

    public void actualizarPuntuacion() {
        this.textViewPuntuacion2.setText(String.valueOf(this.puntuacion));
    }

    public void sumarPunto() {
        this.puntuacion++;
        actualizarPuntuacion();
        reiniciarPos();
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private boolean getRamdomBool() {
        if (Math.random() >= 0.5) {
            return true;
        } else {
            return false;
        }
    }

    public boolean verificarPos() {
        if (anguloActual >= POS_MINIMA && anguloActual <= POS_MAXIMA) {
            return true;
        } else {
            return false;
        }
    }

    private void lanzarProcesoVerificacion() {

        new AsincroTaskVerificarPos(GameActivity.this).execute();
    }

}