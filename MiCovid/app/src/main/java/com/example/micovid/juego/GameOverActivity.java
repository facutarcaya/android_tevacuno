package com.example.micovid.juego;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micovid.R;
import com.example.micovid.actividadprincipal.MainActivity;
import com.example.micovid.asincronico.AsincroTaskEvento;
import com.example.micovid.login.LoginActivity;
import com.example.micovid.pantallaprincipal.PantallaInicioActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class GameOverActivity extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "com.example.micovid.EMAIL_OVER";
    public static final String EXTRA_TOKEN = "com.example.micovid.TOKEN_OVER";
    public static final String EXTRA_REFRESH = "com.example.micovid.REFRESH_OVER";
    public static final String EXTRA_TIEMPO = "com.example.micovid.TIEMPO_OVER";

    private EditText editTextNombrePuntuacion;
    private Button buttonOverVolver;
    private Button buttonOverPuntuaciones;
    private Button buttonGuardar;
    private ProgressBar progressBarEvento;

    private int puntuacion;
    private String email;
    private String token;
    private String tokenRefresh;
    private long tiempoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        this.editTextNombrePuntuacion = findViewById(R.id.editTextNombrePuntuacion);
        this.buttonOverVolver = findViewById(R.id.buttonOverVolver);
        this.buttonOverPuntuaciones = findViewById(R.id.buttonOverPuntuaciones);
        this.buttonGuardar = findViewById(R.id.buttonGuardar);
        this.progressBarEvento = findViewById(R.id.progressBarEvento);
        this.progressBarEvento.setVisibility(View.INVISIBLE);

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
        preguntarVolverInicio();
    }

    public void buttonPreguntarInicio(View view) {
        preguntarVolverInicio();
    }

    public void preguntarVolverInicio() {
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

    public void pausarPantalla() {
        this.buttonGuardar.setEnabled(false);
        this.buttonOverPuntuaciones.setEnabled(false);
        this.buttonOverVolver.setEnabled(false);
        this.editTextNombrePuntuacion.setEnabled(false);
        this.progressBarEvento.setVisibility(View.VISIBLE);
    }

    public void reanudarPantallaError() {
        this.buttonGuardar.setEnabled(true);
        this.buttonOverPuntuaciones.setEnabled(true);
        this.buttonOverVolver.setEnabled(true);
        this.editTextNombrePuntuacion.setEnabled(true);
        this.progressBarEvento.setVisibility(View.INVISIBLE);
    }

    public void reanudarPantalla() {
        this.buttonOverPuntuaciones.setEnabled(true);
        this.buttonOverVolver.setEnabled(true);
        this.progressBarEvento.setVisibility(View.INVISIBLE);
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

    public void lanzarRegistro(View view) {
        new AsincroTaskEvento(GameOverActivity.this).execute(this.editTextNombrePuntuacion.getText().toString(),this.puntuacion,this.tokenRefresh);
    }

    public void guardarPuntuacion() {
        Puntuacion nuevaPuntuacion = new Puntuacion(this.editTextNombrePuntuacion.getText().toString(),this.puntuacion);

        String jsonString;
        String newJsonString = "[\n";
        int i = 0;

        try {
            File f = new File("/data/data/" + getApplicationContext().getPackageName() + "/" + "puntuaciones.json");
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer,"UTF-8");
            JSONArray jsonArray = new JSONArray(jsonString);

            for(i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                if(i != 0) {
                    newJsonString = newJsonString + ",";
                }

                Puntuacion viejaPuntuacion = new Puntuacion(obj.getString("nombre"),obj.getString("puntuacion"),obj.getString("fecha"));

                newJsonString = newJsonString + viejaPuntuacion.toJson();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if(i != 0) {
            newJsonString = newJsonString + ",";
        }

        newJsonString = newJsonString + nuevaPuntuacion.toJson() + "\n]";

        Log.i("Puntuaciones", newJsonString);

        try {
            FileWriter file = new FileWriter("/data/data/" + getApplicationContext().getPackageName() + "/" + "puntuaciones.json");
            file.write(newJsonString);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.showMessage("Datos guardados correctamente");
        this.reanudarPantalla();
    }

    public void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}