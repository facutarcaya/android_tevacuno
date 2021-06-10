package com.example.micovid.juego;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.micovid.R;

public class GameActivity extends AppCompatActivity {

    private RotateAnimation rotateAnimation;
    private ImageView imageViewVacunaVerde;
    private ImageView imageViewVacunaRoja;
    private int anguloActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        anguloActual = -90;

        this.imageViewVacunaVerde = findViewById(R.id.imageViewVacunaVerde);
        this.imageViewVacunaRoja = findViewById(R.id.imageViewVacunaRoja);

        this.rotateAnimation = new RotateAnimation(0, anguloActual,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        this.rotateAnimation.setInterpolator(new LinearInterpolator());
        this.rotateAnimation.setDuration(1);
        this.rotateAnimation.setRepeatCount(0);
        this.rotateAnimation.setFillAfter(true);
        imageViewVacunaRoja.startAnimation(this.rotateAnimation);
    }

    public void rotarMas(View view) {
        rotateImage(10);
    }

    public void rotarMenos(View view) {
        rotateImage(-10);
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
        this.anguloActual = anguloActual + degrees;
        Log.d("Angulos", "De: " + this.anguloActual + " A: " + degrees);
    }
}