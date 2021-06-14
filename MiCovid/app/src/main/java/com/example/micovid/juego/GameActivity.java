package com.example.micovid.juego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.micovid.R;
import com.example.micovid.asincronico.AsincroTaskVerificarPos;
import com.example.micovid.registrar.RegistrarActivity;

import org.w3c.dom.Text;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private static final int POS_MINIMA = -5;
    private static final int POS_MAXIMA = 5;
    private static final int SEGUNDOS_MAXIMOS = 60;
    public static final String EXTRA_PUNTUACION = "com.example.micovid.GAME_PUNTUACION";

    private RotateAnimation rotateAnimation;
    private TextView textViewPuntuacion2;
    private ImageView imageViewVacunaVerde;
    private ImageView imageViewVacunaRoja;
    private ImageView imageViewGrayBack;
    private ImageView imageViewCountDown;
    private ImageView imageViewNumber1;
    private ImageView imageViewNumber2;

    private float anguloActual;
    private int puntuacion;
    public boolean validando;
    private boolean juegoIniciado;
    private boolean juegoDetenido;
    private int countdown;
    private long segundos;
    private long segundosPausa;

    private SensorManager mSensorManager;
    private Sensor mGyro;
    private Sensor mLight;
    private TextView textViewLight;

    private float valorXprevio;
    private float valorXactual;
    private float valorYprevio;
    private float valorYactual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.imageViewVacunaVerde = findViewById(R.id.imageViewVacunaVerde);
        this.imageViewVacunaRoja = findViewById(R.id.imageViewVacunaRoja);
        this.textViewPuntuacion2 = findViewById(R.id.textViewPuntuacion2);
        this.imageViewGrayBack = findViewById(R.id.imageViewGrayBack);
        this.imageViewCountDown = findViewById(R.id.imageViewCountDown);
        this.imageViewNumber1 = findViewById(R.id.imageViewNumber1);
        this.imageViewNumber2 = findViewById(R.id.imageViewNumber2);

        this.textViewLight = findViewById(R.id.textViewLight);

        this.imageViewNumber1.setVisibility(View.INVISIBLE);
        this.imageViewNumber2.setVisibility(View.INVISIBLE);

        countdown = 3;
        juegoIniciado = false;
        juegoDetenido = false;

        this.mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        this.mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (this.mGyro != null) {
            mSensorManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.d("ERROR","GYRO NO SOPORTADO");
        }

        if (this.mGyro != null) {
            mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.d("ERROR","LIGHT NO SOPORTADO");
        }

        puntuacion = 0;
        validando = false;
        valorXprevio = 0;
        valorYprevio = 0;
        segundos = 0;
        segundosPausa = 0;


        reiniciarPos();
        actualizarPuntuacion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.segundos = System.currentTimeMillis() - segundosPausa;
        mSensorManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.segundosPausa = System.currentTimeMillis() - this.segundos;
    }

    public void rotarMas(View view) {
        rotateImage(5);
    }

    public void rotarMenos(View view) {
        rotateImage(-5);
    }

    public void rotateImage(float degrees) {
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (juegoIniciado && !juegoDetenido) {

            if (countdown == SEGUNDOS_MAXIMOS) {
                Resources res = getResources();
                this.imageViewNumber1.setImageResource(res.getIdentifier("number" + (countdown/10) + "_png" , "drawable", getPackageName()));
                this.imageViewNumber2.setImageResource(res.getIdentifier("number" + (countdown%10) + "_png" , "drawable", getPackageName()));
                segundos = System.currentTimeMillis();
                countdown--;
            } else if(System.currentTimeMillis() - segundos >= 1000) {
                if(countdown < 0) {
                    detenerJuego();
                } else {
                    Resources res = getResources();
                    this.imageViewNumber1.setImageResource(res.getIdentifier("number" + (countdown/10) + "_png" , "drawable", getPackageName()));
                    this.imageViewNumber2.setImageResource(res.getIdentifier("number" + (countdown%10) + "_png" , "drawable", getPackageName()));
                    countdown--;
                }
                segundos = System.currentTimeMillis();
            }

            Sensor sensor = sensorEvent.sensor;

            if(sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                valorXactual = sensorEvent.values[0];
                valorYactual = sensorEvent.values[1];

                if (valorXprevio == 0 && valorYprevio == 0) {
                    valorXprevio = valorXactual;
                    valorYprevio = valorXactual;
                } else {
                    if( valorXactual < valorXprevio ) {
                        rotateImage((float) (Math.abs( valorXactual - valorXprevio ) * 10.0 * 1.5));
                    } else {
                        rotateImage((float) (Math.abs( valorXactual - valorXprevio ) * -10.0 * 1.5));
                    }

                    valorXprevio = valorXactual;
                    valorYprevio = valorXactual;

                }
            } else if (sensor.getType() == Sensor.TYPE_LIGHT) {
                textViewLight.setText(String.valueOf(sensorEvent.values[0]));
            }
        } else if (!juegoDetenido && !juegoIniciado){
            if(countdown == 3) {
                segundos = System.currentTimeMillis();
                countdown--;
            } else if(System.currentTimeMillis() - segundos >= 1000) {
                if(countdown == 0) {
                    iniciarJuego();
                } else {
                    Resources res = getResources();
                    this.imageViewCountDown.setImageResource(res.getIdentifier("number" + countdown + "_png" , "drawable", getPackageName()));
                    countdown--;
                }
                segundos = System.currentTimeMillis();
            }
        }

    }

    public void iniciarJuego() {
        this.imageViewCountDown.setVisibility(View.GONE);
        this.imageViewGrayBack.setVisibility(View.GONE);
        this.imageViewNumber1.setVisibility(View.VISIBLE);
        this.imageViewNumber2.setVisibility(View.VISIBLE);
        juegoIniciado = true;
        juegoDetenido = false;
        countdown = SEGUNDOS_MAXIMOS;
    }

    public void detenerJuego() {
        juegoIniciado = false;
        juegoDetenido = true;

        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra(EXTRA_PUNTUACION, String.valueOf(this.puntuacion));
        startActivity(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}