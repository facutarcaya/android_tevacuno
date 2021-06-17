package com.example.micovid.pantallaprincipal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micovid.R;
import com.example.micovid.actividadprincipal.MainActivity;
import com.example.micovid.asincronico.AsincroTaskRefresh;
import com.example.micovid.asincronico.AsincroTaskVerificarTimeout;
import com.example.micovid.juego.GameActivity;
import com.example.micovid.juego.GameOverActivity;
import com.example.micovid.juego.PuntuacionesActivity;
import com.example.micovid.login.LoginActivity;
import com.example.micovid.registrar.RegistrarActivity;

public class PantallaInicioActivity extends AppCompatActivity {
    public static final String EXTRA_TIEMPO = "com.example.micovid.TIEMPO_INICIO";
    public static final String EXTRA_EMAIL = "com.example.micovid.EMAIL_INICIO";
    public static final String EXTRA_TOKEN = "com.example.micovid.TOKEN_INICIO";
    public static final String EXTRA_REFRESH = "com.example.micovid.REFRESH_INICIO";

    public static final String PREFS_ULTIMA_PUNTUACION = "UltPuntuacion" ;
    public static final String KEY_ULTIMA_PUNTUACION = "KeyUltimaPuntuacion";

    private TextView textViewBatteryLevel;
    private TextView textViewUltPts;
    private ImageView imageViewBatteryLevel;
    private ProgressBar progressBarTokenRefresh;
    private Button buttonJugar;
    private boolean esperando;
    private long tiempoActual;

    private String email;
    private String token;
    private String tokenRefresh;

    private AsincroTaskVerificarTimeout asincroTaskVerificarTimeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio);

        this.textViewBatteryLevel = findViewById(R.id.textViewBatteryLevel);
        this.imageViewBatteryLevel = findViewById(R.id.imageViewBatteryLevel);
        this.progressBarTokenRefresh = findViewById(R.id.progressBarTokenRefresh);
        this.buttonJugar = findViewById(R.id.buttonJugar);
        this.textViewUltPts = findViewById(R.id.textViewUltPts);

        updateBattery();
        reanudarPantalla();


        Intent intent = getIntent();

        String message = intent.getStringExtra(LoginActivity.EXTRA_EMAIL);
        if (message == null) {
            message = intent.getStringExtra(RegistrarActivity.EXTRA_EMAIL);

            if(message == null) {
                message = intent.getStringExtra(GameOverActivity.EXTRA_EMAIL);

                if(message == null) {
                    this.email = intent.getStringExtra(GameActivity.EXTRA_EMAIL);
                    this.token = intent.getStringExtra(GameActivity.EXTRA_TOKEN);
                    this.tokenRefresh = intent.getStringExtra(GameActivity.EXTRA_REFRESH);
                    this.tiempoActual = Long.parseLong(intent.getStringExtra(GameActivity.EXTRA_TIEMPO));
                } else {
                    this.email = intent.getStringExtra(message);
                    this.token = intent.getStringExtra(GameOverActivity.EXTRA_TOKEN);
                    this.tokenRefresh = intent.getStringExtra(GameOverActivity.EXTRA_REFRESH);
                    this.tiempoActual = Long.parseLong(intent.getStringExtra(GameOverActivity.EXTRA_TIEMPO));
                }
            } else {
                this.email = message;
                this.token = intent.getStringExtra(RegistrarActivity.EXTRA_TOKEN);
                this.tokenRefresh = intent.getStringExtra(RegistrarActivity.EXTRA_REFRESH);
                this.tiempoActual = System.currentTimeMillis();
            }

        } else {
            this.email = message;
            this.token = intent.getStringExtra(LoginActivity.EXTRA_TOKEN);
            this.tokenRefresh = intent.getStringExtra(LoginActivity.EXTRA_REFRESH);
            this.tiempoActual = System.currentTimeMillis();
        }

        SharedPreferences sharedpreferences = getSharedPreferences(PREFS_ULTIMA_PUNTUACION, Context.MODE_PRIVATE);

        int ultPreferencia = sharedpreferences.getInt(KEY_ULTIMA_PUNTUACION,-1);

        if (ultPreferencia == -1) {
            this.textViewUltPts.setText("No registrada");
        } else {
            this.textViewUltPts.setText(String.valueOf(ultPreferencia) + " dosis");
        }

    }

    public void preguntarRefresh() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Venció la sesión");
        alertDialogBuilder.setIcon(R.drawable.refresh);
        alertDialogBuilder.setMessage("¿Desea renovar la sesión?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                lanzarRefrescarToken();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMessage("Cerró la sesión correctamente");
                volverAlInicio();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void iniciarJuego(View view) {
        cancelarTask();
        finish();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_TIEMPO,String.valueOf(this.tiempoActual));
        intent.putExtra(EXTRA_EMAIL, this.email);
        intent.putExtra(EXTRA_TOKEN, this.token);
        intent.putExtra(EXTRA_REFRESH, this.tokenRefresh);
        startActivity(intent);
    }

    public void volverAlInicio() {
        cancelarTask();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void lanzarRefrescarToken() {
        new AsincroTaskRefresh(PantallaInicioActivity.this).execute(this.tokenRefresh);
    }

    public void pausarPantalla() {
        this.progressBarTokenRefresh.setVisibility(View.VISIBLE);
        this.buttonJugar.setEnabled(false);
        this.esperando = true;
    }

    public void reanudarPantalla() {
        this.progressBarTokenRefresh.setVisibility(View.INVISIBLE);
        this.buttonJugar.setEnabled(true);
        this.esperando = false;
    }

    public void lanzarTask() {
        asincroTaskVerificarTimeout = new AsincroTaskVerificarTimeout(PantallaInicioActivity.this);
        asincroTaskVerificarTimeout.execute(this.tiempoActual);
    }

    public void cancelarTask() {
        asincroTaskVerificarTimeout.cancel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBattery();
        lanzarTask();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelarTask();
    }

    private void updateBattery() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        textViewBatteryLevel.setText(String.valueOf(level) + " %");

        if (isCharging) {
            imageViewBatteryLevel.setImageResource(R.drawable.battery_charging);
        } else {
            if (level > 75) {
                imageViewBatteryLevel.setImageResource(R.drawable.battery_100);
            } else if (level > 50) {
                imageViewBatteryLevel.setImageResource(R.drawable.battery_75);
            } else if(level > 25) {
                imageViewBatteryLevel.setImageResource(R.drawable.battery_50);
            } else {
                imageViewBatteryLevel.setImageResource(R.drawable.battery_25);
            }
        }
    }

    public void refrescarTokens(String token, String tokenRefresh) {
        this.token = token;
        this.tokenRefresh = tokenRefresh;
        this.tiempoActual = System.currentTimeMillis();
        this.lanzarTask();
    }

    public void mostrarPuntuaciones(View view) {
        Intent intent = new Intent(this, PuntuacionesActivity.class);
        startActivity(intent);
    }

    public void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Salir");
        alertDialogBuilder.setMessage("¿Desea salir al inicio?");
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

    public void abrirComoJugar(View view) {
        Intent intent = new Intent(this, ComoJugarActivity.class);
        startActivity(intent);
    }

}