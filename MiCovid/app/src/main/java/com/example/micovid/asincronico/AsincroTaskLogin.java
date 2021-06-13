package com.example.micovid.asincronico;

import android.os.AsyncTask;
import android.util.Log;

import com.example.micovid.actividadprincipal.MainActivity;
import com.example.micovid.actividadprincipal.Usuario;
import com.example.micovid.comm.Communication;
import com.example.micovid.login.LoginActivity;
import com.example.micovid.pantallaprincipal.PantallaInicioActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsincroTaskLogin extends AsyncTask<Object, Void, Boolean> {
    private LoginActivity loginActivity;
    private String mensaje;
    private Usuario usuario;


    public AsincroTaskLogin(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    @Override
    protected void onPreExecute() {
        this.loginActivity.toggleProgressBar(true);
        this.loginActivity.habilitarBotones(false);
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        Communication communication = new Communication();

        String respuesta = communication.loginUsuario(objects[0].toString(),objects[1].toString());


        if(respuesta.compareTo(communication.MSG_ERROR) == 0) {
            this.mensaje = "Error en la conexión, intente de nuevo más tarde";
            return false;
        }

        JSONObject answer = null;
        try {
            answer = new JSONObject(respuesta);

            if (answer.get("success").toString().compareTo("true") == 0) {
                this.usuario = new Usuario(objects[0].toString(),answer.get("token").toString(),answer.get("token_refesh").toString());

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
        this.loginActivity.toggleProgressBar(false);
        if(aBoolean) {
            this.loginActivity.lanzarActivity(PantallaInicioActivity.class, this.usuario);
            this.loginActivity.showMessage("Datos correctos");
        } else {
            this.loginActivity.habilitarBotones(true);
            this.loginActivity.showMessage(this.mensaje);
        }
        super.onPostExecute(aBoolean);
    }
}
