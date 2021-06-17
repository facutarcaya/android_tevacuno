package com.example.micovid.juego;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.micovid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PuntuacionesActivity extends Activity {

    private TableLayout tableLayout;

    private List<Puntuacion> puntuaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuaciones);

        this.setFinishOnTouchOutside(false);

        this.tableLayout = findViewById(R.id.table_layout);

        this.cargarEnTablaFecha();

    }

    public void cargarEnTablaFecha() {
        cargarPuntuacionesEnLista();

        for(int i= this.puntuaciones.size() - 1; i >= 0; i--) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText(this.puntuaciones.get(i).getNombre());
            t1v.setTextColor(Color.GRAY);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText(this.puntuaciones.get(i).getPuntuacion());
            t2v.setTextColor(Color.GRAY);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText(this.puntuaciones.get(i).getFecha());
            t3v.setTextColor(Color.GRAY);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            this.tableLayout.addView(tbrow);
        }
    }

    public void cargarPuntuacionesEnLista () {
        this.puntuaciones = new ArrayList<>();
        String jsonString;
        try {
            File f = new File("/data/data/" + getApplicationContext().getPackageName() + "/" + "puntuaciones.json");
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer,"UTF-8");
            JSONArray jsonArray = new JSONArray(jsonString);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                Puntuacion viejaPuntuacion = new Puntuacion(obj.getString("nombre"),obj.getString("puntuacion"),obj.getString("fecha"));

                this.puntuaciones.add(viejaPuntuacion);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    }