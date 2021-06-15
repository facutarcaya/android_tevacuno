package com.example.micovid.juego;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.micovid.R;
import com.example.micovid.actividadprincipal.MainActivity;
import com.example.micovid.login.LoginActivity;
import com.example.micovid.pantallaprincipal.PantallaInicioActivity;

public class GameOverActivity extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "com.example.micovid.EMAIL_OVER";
    public static final String EXTRA_TOKEN = "com.example.micovid.TOKEN_OVER";
    public static final String EXTRA_REFRESH = "com.example.micovid.REFRESH_OVER";
    public static final String EXTRA_TIEMPO = "com.example.micovid.TIEMPO_OVER";

    private int puntuacion;
    private String email;
    private String token;
    private String tokenRefresh;
    private long tiempoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();
        String msg_puntuacion = intent.getStringExtra(GameActivity.EXTRA_PUNTUACION);
        this.puntuacion = Integer.parseInt(msg_puntuacion);
        this.email = intent.getStringExtra(GameActivity.EXTRA_EMAIL);
        this.token = intent.getStringExtra(GameActivity.EXTRA_TOKEN);
        this.tokenRefresh = intent.getStringExtra(GameActivity.EXTRA_REFRESH);
        this.tiempoActual = Long.parseLong(intent.getStringExtra(GameActivity.EXTRA_TIEMPO));

        TextView textViewPuntuacion = findViewById(R.id.textViewPuntuacion);
        textViewPuntuacion.setText(msg_puntuacion);
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

    public void volverAlInicio() {
        finish();
        Intent intent = new Intent(this, PantallaInicioActivity.class);
        intent.putExtra(EXTRA_TIEMPO,String.valueOf(this.tiempoActual));
        intent.putExtra(EXTRA_EMAIL, this.email);
        intent.putExtra(EXTRA_TOKEN, this.token);
        intent.putExtra(EXTRA_REFRESH, this.tokenRefresh);
        startActivity(intent);
    }
}