package com.example.micovid.asincronico;

import android.os.AsyncTask;

import com.example.micovid.comm.Communication;
import com.example.micovid.juego.GameOverActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AsincroTaskEvento extends AsyncTask<Object, Void, Boolean> {
    private GameOverActivity gameOverActivity;
    private String mensaje;

    public AsincroTaskEvento(GameOverActivity gameOverActivity) {
        this.gameOverActivity = gameOverActivity;
    }

    @Override
    protected void onPreExecute() {
        this.gameOverActivity.pausarPantalla();
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        Communication communication = new Communication();

        String respuesta = communication.registrarEvento(objects[0].toString(),Integer.valueOf(objects[1].toString()),objects[2].toString());

        if(respuesta.compareTo(communication.MSG_ERROR) == 0) {
            this.mensaje = "Error en la conexión, intente de nuevo más tarde";
            return false;
        }

        JSONObject answer = null;
        try {
            answer = new JSONObject(respuesta);

            if (answer.get("success").toString().compareTo("true") == 0) {
                return true;
            } else {
                this.mensaje = answer.get("msg").toString();

                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            this.mensaje = "Error inesperado, intente nuevamente";
            return false;
        }
    }
    public static StringBuilder convertInputStreamToString(InputStreamReader inputStreamReader) throws IOException {
        BufferedReader br = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ( (line = br.readLine()) != null ){
            stringBuilder.append(line + "\n");
        }
        br.close();
        return stringBuilder;
    }
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean) {
            this.gameOverActivity.guardarPuntuacion();
        } else {
            this.gameOverActivity.reanudarPantallaError();
            this.gameOverActivity.showMessage(this.mensaje);
        }
        super.onPostExecute(aBoolean);
    }

}
