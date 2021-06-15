package com.example.micovid.juego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.micovid.R;
import com.example.micovid.asincronico.AsincroTaskVerificarPos;
import com.example.micovid.pantallaprincipal.PantallaInicioActivity;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    public static final String EXTRA_PUNTUACION = "com.example.micovid.GAME_PUNTUACION";
    public static final String EXTRA_TIEMPO = "com.example.micovid.TIEMPO_GAME";
    public static final String EXTRA_EMAIL = "com.example.micovid.EMAIL_GAME";
    public static final String EXTRA_TOKEN = "com.example.micovid.TOKEN_GAME";
    public static final String EXTRA_REFRESH = "com.example.micovid.REFRESH_GAME";

    private static final int POS_MINIMA = -5;
    private static final int POS_MAXIMA = 5;
    private static final int SEGUNDOS_MAXIMOS = 60;
    private static final float UMBRAL_LUZ_MINIMA = (float) 20.0;

    private RotateAnimation rotateAnimation;
    private TextView textViewXGyro;
    private TextView textViewYGyro;
    private TextView textViewZGyro;
    private TextView textViewPuntuacionValor;
    private TextView textViewPocaLuz;
    private ImageView imageViewVacunaVerde;
    private ImageView imageViewVacunaRoja;
    private ConstraintLayout fondoPausa;
    private ImageView imageViewCountDown;
    private ImageView imageViewNumber1;
    private ImageView imageViewNumber2;

    private float anguloActual;
    private int puntuacion;
    public boolean validando;
    private boolean juegoIniciado;
    private boolean juegoDetenido;
    private boolean juegoPausado;
    private int countdown;
    private long segundos;
    private long segundosPausa;

    private SensorManager mSensorManager;
    private Sensor mGyro;
    private Sensor mLight;
    private TextView textViewLight;
    private float valorLuz;

    private float valorXprevio;
    private float valorXactual;
    private float valorYprevio;
    private float valorYactual;

    private String email;
    private String token;
    private String tokenRefresh;
    private long tiempoActual;

    private Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.imageViewVacunaVerde = findViewById(R.id.imageViewVacunaVerde);
        this.imageViewVacunaRoja = findViewById(R.id.imageViewVacunaRoja);
        this.textViewXGyro = findViewById(R.id.textViewXGyro);
        this.textViewYGyro = findViewById(R.id.textViewYGyro);
        this.textViewZGyro = findViewById(R.id.textViewZGyro);
        this.textViewXGyro.setText("X: 0°");
        this.textViewYGyro.setText("Y: 0°");
        this.textViewZGyro.setText("Z: 0°");
        this.textViewPocaLuz = findViewById(R.id.textViewPocaLuz);
        this.textViewPuntuacionValor = findViewById(R.id.textViewPuntuacionValor);
        this.fondoPausa = findViewById(R.id.fondoPausa);
        this.imageViewCountDown = findViewById(R.id.imageViewCountDown);
        this.imageViewNumber1 = findViewById(R.id.imageViewNumber1);
        this.imageViewNumber2 = findViewById(R.id.imageViewNumber2);

        this.textViewLight = findViewById(R.id.textViewLight);

        this.imageViewNumber1.setVisibility(View.INVISIBLE);
        this.imageViewNumber2.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();

        this.valorLuz = (float) 0.0;

        this.email = intent.getStringExtra(PantallaInicioActivity.EXTRA_EMAIL);
        this.token = intent.getStringExtra(PantallaInicioActivity.EXTRA_TOKEN);
        this.tokenRefresh = intent.getStringExtra(PantallaInicioActivity.EXTRA_REFRESH);
        this.tiempoActual = Long.parseLong(intent.getStringExtra(PantallaInicioActivity.EXTRA_TIEMPO));

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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
        juegoPausado = false;

        this.textViewPocaLuz.setVisibility(View.INVISIBLE);

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

    public void validarLuz(float valorLuz) {
        if(valorLuz >= UMBRAL_LUZ_MINIMA && juegoPausado) {
            reanudarJuego();
        } else if (valorLuz < UMBRAL_LUZ_MINIMA && !juegoPausado) {
            pausarJuego();
        }
    }

    private void pausarJuego() {
        this.juegoPausado = true;
        this.fondoPausa.setVisibility(View.VISIBLE);
        this.textViewPocaLuz.setVisibility(View.VISIBLE);
        this.segundosPausa = System.currentTimeMillis() - this.segundos;
    }

    private void reanudarJuego() {
        this.juegoPausado = false;
        this.fondoPausa.setVisibility(View.INVISIBLE);
        this.textViewPocaLuz.setVisibility(View.INVISIBLE);
        this.segundos = System.currentTimeMillis() - segundosPausa;
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
        this.textViewPuntuacionValor.setText(String.valueOf(this.puntuacion));
    }

    public void sumarPunto() {
        this.puntuacion++;
        v.vibrate(VibrationEffect.createOneShot(200,VibrationEffect.DEFAULT_AMPLITUDE));
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

        Sensor sensor = sensorEvent.sensor;

        if (juegoIniciado && !juegoDetenido) {

            if (sensor.getType() == Sensor.TYPE_LIGHT) {
                this.valorLuz = (float) sensorEvent.values[0];
                textViewLight.setText(String.valueOf(this.valorLuz) + " lx");
                validarLuz(this.valorLuz);
            }

            if(!juegoPausado) {


                if (countdown == SEGUNDOS_MAXIMOS) {
                    Resources res = getResources();
                    this.imageViewNumber1.setImageResource(res.getIdentifier("number" + (countdown / 10) + "_png", "drawable", getPackageName()));
                    this.imageViewNumber2.setImageResource(res.getIdentifier("number" + (countdown % 10) + "_png", "drawable", getPackageName()));
                    segundos = System.currentTimeMillis();
                    countdown--;
                } else if (System.currentTimeMillis() - segundos >= 1000) {
                    if (countdown < 0) {
                        detenerJuego();
                    } else {
                        Resources res = getResources();
                        this.imageViewNumber1.setImageResource(res.getIdentifier("number" + (countdown / 10) + "_png", "drawable", getPackageName()));
                        this.imageViewNumber2.setImageResource(res.getIdentifier("number" + (countdown % 10) + "_png", "drawable", getPackageName()));
                        countdown--;
                    }
                    segundos = System.currentTimeMillis();
                }

                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                    this.textViewXGyro.setText("X: " + sensorEvent.values[0] + "°");
                    this.textViewYGyro.setText("Y: " + sensorEvent.values[1] + "°");
                    this.textViewZGyro.setText("Z: " + sensorEvent.values[2] + "°");

                    valorXactual = sensorEvent.values[0];
                    valorYactual = sensorEvent.values[1];

                    if (valorXprevio == 0 && valorYprevio == 0) {
                        valorXprevio = valorXactual;
                        valorYprevio = valorXactual;
                    } else {
                        if (valorXactual < valorXprevio) {
                            rotateImage((float) (Math.abs(valorXactual - valorXprevio) * 10.0 * 1.5));
                        } else {
                            rotateImage((float) (Math.abs(valorXactual - valorXprevio) * -10.0 * 1.5));
                        }

                        valorXprevio = valorXactual;
                        valorYprevio = valorXactual;

                    }
                }
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
        this.fondoPausa.setVisibility(View.INVISIBLE);
        this.imageViewNumber1.setVisibility(View.VISIBLE);
        this.imageViewNumber2.setVisibility(View.VISIBLE);
        juegoIniciado = true;
        juegoDetenido = false;
        countdown = SEGUNDOS_MAXIMOS;
    }

    public void detenerJuego() {
        juegoIniciado = false;
        juegoDetenido = true;

        finish();

        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra(EXTRA_PUNTUACION, String.valueOf(this.puntuacion));
        intent.putExtra(EXTRA_TIEMPO,String.valueOf(this.tiempoActual));
        intent.putExtra(EXTRA_EMAIL, this.email);
        intent.putExtra(EXTRA_TOKEN, this.token);
        intent.putExtra(EXTRA_REFRESH, this.tokenRefresh);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Salir");
        alertDialogBuilder.setMessage("¿Desea terminar el juego y salir?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                volverAlInicio();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void volverAlInicio() {
        finish();
        Intent intent = new Intent(this, PantallaInicioActivity.class);
        intent.putExtra(EXTRA_TIEMPO,String.valueOf(this.tiempoActual));
        intent.putExtra(EXTRA_EMAIL, this.email);
        intent.putExtra(EXTRA_TOKEN, this.token);
        intent.putExtra(EXTRA_REFRESH, this.tokenRefresh);
        startActivity(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}