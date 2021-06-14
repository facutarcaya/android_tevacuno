package com.example.micovid.asincronico;

import android.os.AsyncTask;

import com.example.micovid.actividadprincipal.Usuario;
import com.example.micovid.comm.Communication;
import com.example.micovid.pantallaprincipal.PantallaInicioActivity;
import com.example.micovid.registrar.RegistrarActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AsincroTaskRefresh extends AsyncTask<Object, Void, Boolean> {
    private PantallaInicioActivity pantallaInicioActivity;
    private String mensaje;
    private String token;
    private String tokenRefresh;


    public AsincroTaskRefresh(PantallaInicioActivity pantallaInicioActivity) {
        this.pantallaInicioActivity = pantallaInicioActivity;
    }

    @Override
    protected void onPreExecute() {
        this.pantallaInicioActivity.pausarPantalla();
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
        this.pantallaInicioActivity.reanudarPantalla();
        if(aBoolean) {
            this.pantallaInicioActivity.showMessage("Se refresco el token correctamente");
            this.pantallaInicioActivity.refrescarTokens(this.token,this.tokenRefresh);
        } else {
            this.pantallaInicioActivity.showMessage("Error al resfrescar el token, vuelva a loguearse");
            this.pantallaInicioActivity.volverAlInicio();
        }
        super.onPostExecute(aBoolean);
    }
}
