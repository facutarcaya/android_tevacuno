package com.example.micovid.asincronico;

import android.os.AsyncTask;

import com.example.micovid.actividadprincipal.Usuario;
import com.example.micovid.comm.Communication;
import com.example.micovid.pantallaprincipal.PantallaInicioActivity;
import com.example.micovid.registrar.RegistrarActivity;
import com.example.micovid.renovartoken.RefreshTokenActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AsincroTaskRefresh extends AsyncTask<Object, Void, Boolean> {
    private RefreshTokenActivity refreshTokenActivity;
    private String mensaje;
    private String token;
    private String tokenRefresh;


    public AsincroTaskRefresh(RefreshTokenActivity refreshTokenActivity) {
        this.refreshTokenActivity = refreshTokenActivity;
    }

    @Override
    protected void onPreExecute() {
        //this.refreshTokenActivity.toggleProgressBar(true);
        //this.refreshTokenActivity.habilitarBotones(false);
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        Communication communication = new Communication();

        String respuesta = communication.refrescarToken(objects[0].toString());

        if(respuesta.compareTo(communication.MSG_ERROR) == 0) {
            this.mensaje = "Error en la conexión, intente de nuevo más tarde";
            return false;
        }

        JSONObject answer = null;
        try {
            answer = new JSONObject(respuesta);

            if (answer.get("success").toString().compareTo("true") == 0) {
                this.token = answer.get("token").toString();
                this.tokenRefresh = answer.get("token_refresh").toString();

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
        //this.refreshTokenActivity.toggleProgressBar(false);
        if(aBoolean) {
            //this.registrarActivity.lanzarActivity(PantallaInicioActivity.class, this.usuario);
            this.refreshTokenActivity.showMessage("Datos correctos");
        } else {
            //this.refreshTokenActivity.habilitarBotones(true);
            this.refreshTokenActivity.showMessage(this.mensaje);
        }
        super.onPostExecute(aBoolean);
    }
}
